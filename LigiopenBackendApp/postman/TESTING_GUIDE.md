# LigiOpen API Testing Guide

Comprehensive guide for testing the LigiOpen API using Postman collections and automated test runners.

## 🎯 Quick Start

### Option 1: Postman GUI
1. Import `LigiOpen-API-Collection.json` into Postman
2. Import `LigiOpen-Development-Environment.json`
3. Select the development environment
4. Run individual requests or entire collection

### Option 2: Automated Newman Testing
```bash
# Navigate to postman directory
cd postman

# Install dependencies
npm install

# Run development tests
npm run test:dev

# Run with exported reports
npm run test:export
```

## 📋 Complete Testing Workflow

### 1. Environment Setup
Choose your testing environment:

**Development (Recommended for testing)**
- URL: `http://localhost:8000`
- Environment: `LigiOpen-Development-Environment.json`
- Features: Full logging, mock email service

**Production**
- URL: `https://api.ligiopen.com`
- Environment: `LigiOpen-Production-Environment.json`
- Features: Real email service, production data

### 2. Authentication Flow Testing

#### Step 1: User Registration
```json
POST /api/v1/auth/signup
{
  "username": "testuser123",
  "email": "testuser@ligiopen.com", 
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+254712345678",
  "preferredLanguage": "en"
}
```
**Expected**: 200 OK, user created, email verification required

#### Step 2: Email Verification
1. Check application console logs for verification link
2. Extract token from logs: `token=abc123...`
3. Run verification request:
```
GET /api/v1/auth/verify-email?token=abc123...
```
**Expected**: 200 OK, account verified

#### Step 3: User Login  
```json
POST /api/v1/auth/signin
{
  "email": "testuser@ligiopen.com",
  "password": "SecurePassword123!"
}
```
**Expected**: 200 OK, JWT tokens returned and auto-saved

#### Step 4: Token Refresh
```json
POST /api/v1/auth/refresh
{
  "refreshToken": "{{refresh_token}}"
}
```
**Expected**: 200 OK, new tokens returned and updated

### 3. Profile Management Testing

#### Get Current Profile
```
GET /api/v1/users/profile
Authorization: Bearer {{jwt_token}}
```
**Expected**: 200 OK, complete profile with statistics

#### Update Profile
```json
PUT /api/v1/users/profile
Authorization: Bearer {{jwt_token}}
{
  "firstName": "John Updated",
  "lastName": "Doe Updated", 
  "bio": "Updated bio information",
  "preferredLanguage": "sw"
}
```
**Expected**: 200 OK, updated profile returned

#### Upload Profile Picture
```
POST /api/v1/users/profile/picture
Authorization: Bearer {{jwt_token}}
Content-Type: multipart/form-data

Form Data:
- file: [image_file.jpg]
```
**Expected**: 200 OK, file uploaded, URLs returned

#### Remove Profile Picture
```
DELETE /api/v1/users/profile/picture
Authorization: Bearer {{jwt_token}}
```
**Expected**: 200 OK, picture removed

### 4. Error Testing Scenarios

#### Authentication Errors
- **Invalid credentials**: Wrong email/password
- **Unverified account**: Login before email verification
- **Expired token**: Use old JWT token
- **Invalid refresh token**: Use invalid refresh token

#### Validation Errors
- **Short password**: Less than 8 characters
- **Invalid email**: Malformed email address
- **Duplicate username**: Try to register existing username
- **Invalid phone**: Wrong phone number format

#### File Upload Errors
- **Wrong file type**: Upload .txt instead of image
- **File too large**: Upload file > 10MB
- **No authentication**: Upload without JWT token

## 🔧 Automated Testing with Newman

### Installation
```bash
# Global installation
npm install -g newman newman-reporter-htmlextra

# Or local installation
cd postman
npm install
```

### Running Tests

#### Development Environment
```bash
# Basic test run
newman run LigiOpen-API-Collection.json \
  -e LigiOpen-Development-Environment.json

# Verbose output
newman run LigiOpen-API-Collection.json \
  -e LigiOpen-Development-Environment.json \
  --reporters cli --reporter-cli-verbose

# Export results
newman run LigiOpen-API-Collection.json \
  -e LigiOpen-Development-Environment.json \
  --reporters cli,json,htmlextra \
  --reporter-json-export results.json \
  --reporter-htmlextra-export report.html
```

#### Using Test Runner Script
```bash
# Basic development test
node run-tests.js development

# Verbose output
node run-tests.js development --verbose

# Export reports
node run-tests.js development --export-results

# Production test
node run-tests.js production --verbose

# Show help
node run-tests.js --help
```

#### NPM Scripts
```bash
# Development tests with verbose output
npm run test:dev

# Production tests  
npm run test:prod

# Development with exported reports
npm run test:export

# Run all environments
npm run test:all

# Show available commands
npm run help
```

### Test Reports

#### Console Output
- ✅ Real-time test execution
- ✅ Pass/fail indicators  
- ✅ Response times
- ✅ Assertion results
- ✅ Summary statistics

#### JSON Report
```json
{
  "collection": { ... },
  "run": {
    "stats": {
      "requests": { "total": 10, "failed": 0 },
      "tests": { "total": 25, "failed": 0 },
      "assertions": { "total": 50, "failed": 0 }
    },
    "timings": { ... },
    "executions": [ ... ]
  }
}
```

#### HTML Report
- 📊 Interactive dashboard
- 📈 Performance charts  
- 📋 Detailed test results
- 🔍 Request/response inspection
- 📱 Mobile-friendly design

## 🧪 Test Validation

### Automated Test Checks

Each request includes comprehensive test scripts:

#### Status Code Validation
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});
```

#### Response Structure Validation  
```javascript
pm.test("Response has required fields", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('status');
    pm.expect(jsonData).to.have.property('data');
});
```

#### Authentication Token Management
```javascript
pm.test("Token saved successfully", function () {
    const jsonData = pm.response.json();
    if (jsonData.data && jsonData.data.accessToken) {
        pm.environment.set('jwt_token', jsonData.data.accessToken);
    }
});
```

#### Data Integrity Validation
```javascript
pm.test("Profile data is valid", function () {
    const profile = pm.response.json().data;
    pm.expect(profile.email).to.be.a('string');
    pm.expect(profile.firstName).to.have.length.above(1);
});
```

### Performance Testing
```javascript
// Global performance test
pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

## 📊 CI/CD Integration

### GitHub Actions
```yaml
name: API Tests
on: [push, pull_request]

jobs:
  api-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
          
      - name: Install Newman
        run: npm install -g newman newman-reporter-htmlextra
        
      - name: Start API Server
        run: |
          mvn spring-boot:run &
          sleep 30
          
      - name: Run API Tests
        run: |
          cd postman
          newman run LigiOpen-API-Collection.json \
            -e LigiOpen-Development-Environment.json \
            --reporters cli,junit \
            --reporter-junit-export test-results.xml
            
      - name: Publish Test Results
        uses: dorny/test-reporter@v1
        if: always()
        with:
          name: API Tests
          path: postman/test-results.xml
          reporter: java-junit
```

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    
    stages {
        stage('Build API') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Start Server') {
            steps {
                sh 'java -jar target/ligiopen-*.jar &'
                sh 'sleep 30'
            }
        }
        
        stage('API Tests') {
            steps {
                dir('postman') {
                    sh '''
                        newman run LigiOpen-API-Collection.json \
                          -e LigiOpen-Development-Environment.json \
                          --reporters cli,json \
                          --reporter-json-export results.json
                    '''
                }
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'postman/results.json'
            publishTestResults testResultsPattern: 'postman/results.json'
        }
    }
}
```

## 🔍 Debugging Tests

### Common Issues

#### 1. Connection Refused
**Symptoms**: Cannot connect to server
**Solutions**:
- Verify server is running: `curl http://localhost:8000/actuator/health`
- Check port configuration
- Confirm base_url environment variable

#### 2. Authentication Failures  
**Symptoms**: 401 Unauthorized responses
**Solutions**:
- Check JWT token in environment variables
- Verify token hasn't expired
- Try refreshing token
- Re-login if necessary

#### 3. Email Verification Issues
**Symptoms**: Cannot login after registration
**Solutions**:
- Check application logs for verification link
- Copy token from logs manually
- Use resend verification endpoint
- Verify email service is working

#### 4. Test Script Failures
**Symptoms**: Tests fail unexpectedly
**Solutions**:
- Check Postman console for error details
- Verify response structure matches expectations  
- Update test scripts if API changes
- Check environment variable values

### Debug Tools

#### Postman Console
1. View → Show Postman Console
2. See all requests/responses
3. View console.log() output
4. Check error messages

#### Newman Verbose Mode
```bash
newman run collection.json -e environment.json --verbose
```

#### Custom Logging
Add debug logs to test scripts:
```javascript
console.log('Current JWT token:', pm.environment.get('jwt_token'));
console.log('Response body:', pm.response.text());
```

## 📈 Performance Benchmarks

### Expected Response Times
- **Authentication**: < 500ms
- **Profile Retrieval**: < 200ms  
- **Profile Update**: < 300ms
- **File Upload**: < 2000ms (varies by file size)
- **Health Check**: < 100ms

### Load Testing
Use Newman with multiple iterations:
```bash
newman run LigiOpen-API-Collection.json \
  -e LigiOpen-Development-Environment.json \
  --iteration-count 10 \
  --delay-request 100
```

## 🔐 Security Testing

### Token Security
- ✅ JWT tokens expire appropriately
- ✅ Refresh tokens work correctly  
- ✅ Unauthorized requests blocked
- ✅ Sensitive data not exposed

### Input Validation
- ✅ SQL injection attempts blocked
- ✅ XSS attempts sanitized
- ✅ File upload restrictions enforced
- ✅ Rate limiting functional

### Authentication Testing
- ✅ Password requirements enforced
- ✅ Email verification required
- ✅ Account lockout after failures
- ✅ Session management secure

## 📚 Additional Resources

### Documentation
- **Swagger UI**: http://localhost:8000/swagger-ui/index.html
- **API Specification**: http://localhost:8000/v3/api-docs
- **Collection Documentation**: Available in Postman after import

### Tools
- **Postman**: https://postman.com
- **Newman**: https://github.com/postmanlabs/newman
- **Newman HTML Reporter**: https://github.com/DannyDainton/newman-reporter-htmlextra

### Learning Resources
- **Postman Learning Center**: https://learning.postman.com
- **Newman Documentation**: https://github.com/postmanlabs/newman
- **API Testing Best Practices**: https://postman.com/api-testing

---

**Testing Guide Version**: 1.0.0  
**Last Updated**: August 2025  
**Compatible Collection Version**: 1.0.0  
**Maintainer**: LigiOpen Development Team