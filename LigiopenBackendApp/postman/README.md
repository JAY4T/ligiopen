# LigiOpen API Postman Collection

This directory contains comprehensive Postman collections and environments for testing the LigiOpen API.

## 📁 Files Overview

### Collections
- **`LigiOpen-API-Collection.json`** - Complete API collection with all endpoints, tests, and documentation

### Environments
- **`LigiOpen-Development-Environment.json`** - Development environment (localhost:8000)
- **`LigiOpen-Production-Environment.json`** - Production environment (api.ligiopen.com)

## 🚀 Quick Start

### 1. Import Collection
1. Open Postman
2. Click **Import** button
3. Select `LigiOpen-API-Collection.json`
4. Collection appears in your workspace

### 2. Import Environment
1. Click **Import** button
2. Select appropriate environment file:
   - Development: `LigiOpen-Development-Environment.json`
   - Production: `LigiOpen-Production-Environment.json`
3. Select the imported environment from dropdown

### 3. Start Testing
1. Run **"User Registration"** to create account
2. Check application logs for email verification link
3. Run **"User Login"** (JWT tokens auto-saved)
4. Test profile endpoints with authentication

## 📊 Collection Structure

### 🔐 Authentication & Authorization
- **User Registration** - Create new account
- **User Login** - Authenticate and get JWT tokens
- **Refresh JWT Token** - Renew expired tokens
- **Verify Email Address** - Email verification
- **Resend Verification Email** - Resend verification

### 👤 User Profile Management
- **Get Current User Profile** - Retrieve your profile
- **Get User Profile by ID** - View other user profiles
- **Update Current User Profile** - Modify profile information
- **Upload Profile Picture** - Add profile image (multipart/form-data)
- **Remove Profile Picture** - Delete current profile image
- **Delete Current User Profile** - ⚠️ Permanently delete account

### 🔧 System & Health Checks
- **API Health Check** - System status monitoring

## 🔑 Authentication Flow

### Automatic Token Management
The collection automatically manages JWT tokens:

1. **Login Success** → Tokens saved to environment
2. **Token Refresh** → New tokens update environment
3. **All Protected Requests** → Auto-include JWT token
4. **Account Deletion** → Tokens cleared from environment

### Manual Token Management
If needed, you can manually set tokens:
```
jwt_token: your_access_token_here
refresh_token: your_refresh_token_here
```

## 🧪 Testing Features

### Automated Tests
Each request includes comprehensive tests:
- ✅ Status code validation
- ✅ Response structure verification
- ✅ Data integrity checks
- ✅ Authentication validation
- ✅ Environment variable management

### Test Results
After running requests, check the **Test Results** tab for:
- Pass/fail status for each test
- Detailed assertions
- Performance metrics
- Console logs

### Global Scripts
- **Pre-request**: Auto-sets base URL, logs request info
- **Test**: Validates response time, logs response details

## 🌍 Environment Variables

### Development Environment
| Variable | Description | Auto-Set |
|----------|-------------|----------|
| `base_url` | http://localhost:8000 | ❌ |
| `jwt_token` | Access token | ✅ |
| `refresh_token` | Refresh token | ✅ |
| `user_id` | Current user ID | ✅ |
| `user_email` | Current user email | ✅ |
| `test_username` | Test username | ❌ |
| `test_email` | Test email | ❌ |
| `test_password` | Test password | ❌ |

### Production Environment
| Variable | Description | Auto-Set |
|----------|-------------|----------|
| `base_url` | https://api.ligiopen.com | ❌ |
| `jwt_token` | Access token | ✅ |
| `refresh_token` | Refresh token | ✅ |
| `user_id` | Current user ID | ✅ |
| `user_email` | Current user email | ✅ |

## 📖 Detailed Request Documentation

### Authentication Endpoints

#### 📝 User Registration
- **Method**: POST `/api/v1/auth/signup`
- **Body**: JSON with user details
- **Tests**: Status validation, user creation
- **Auto-saves**: User email to environment

#### 🔓 User Login  
- **Method**: POST `/api/v1/auth/signin`
- **Body**: Email/password JSON
- **Tests**: Authentication success, token validation
- **Auto-saves**: JWT tokens, user info

#### 🔄 Refresh Token
- **Method**: POST `/api/v1/auth/refresh` 
- **Body**: Refresh token JSON
- **Tests**: Token renewal validation
- **Auto-updates**: Both access and refresh tokens

### Profile Management Endpoints

#### 👤 Get Current Profile
- **Method**: GET `/api/v1/users/profile`
- **Auth**: Bearer token (automatic)
- **Tests**: Authentication, profile structure
- **Returns**: Complete user profile with statistics

#### ✏️ Update Profile
- **Method**: PUT `/api/v1/users/profile`
- **Auth**: Bearer token (automatic)
- **Body**: Partial profile update JSON
- **Tests**: Update success, field validation

#### 📷 Upload Profile Picture
- **Method**: POST `/api/v1/users/profile/picture`
- **Auth**: Bearer token (automatic)
- **Body**: multipart/form-data with file
- **Tests**: File upload success, URL generation
- **Requirements**: Image file, max 10MB

## 🔍 Troubleshooting

### Common Issues

#### 1. Authentication Failures
**Problem**: 401 Unauthorized responses
**Solutions**:
- Ensure you've logged in recently
- Check `jwt_token` environment variable
- Try refreshing token if expired
- Re-login if refresh fails

#### 2. Email Verification
**Problem**: Cannot login after registration
**Solutions**:
- Check application console logs for verification link
- Copy verification token from logs
- Use "Verify Email Address" request manually
- Try "Resend Verification Email"

#### 3. File Upload Issues
**Problem**: Profile picture upload fails
**Solutions**:
- Ensure file is image type (JPG, PNG)
- Check file size (max 10MB)
- Verify multipart/form-data content type
- Check authentication token

#### 4. Environment Variables
**Problem**: Variables not updating automatically
**Solutions**:
- Check test scripts are enabled
- Verify environment is selected
- Manually refresh environment
- Re-import collection if needed

### Debug Mode
Enable Postman console for detailed logging:
1. View → Show Postman Console
2. Console shows all request/response details
3. Custom log messages from test scripts
4. Error details and stack traces

## 📝 Testing Scenarios

### Complete User Journey
1. **Register** → Create account
2. **Verify Email** → Check logs, verify account
3. **Login** → Authenticate (tokens auto-saved)
4. **Get Profile** → View current profile
5. **Update Profile** → Modify information
6. **Upload Picture** → Add profile image
7. **View Updated Profile** → Confirm changes

### Error Handling Tests
1. **Invalid Registration** → Test validation
2. **Wrong Credentials** → Test auth failure
3. **Expired Token** → Test token refresh
4. **Invalid File Upload** → Test file validation
5. **Unauthorized Access** → Test security

### Performance Tests
- Monitor response times in test results
- Global test ensures < 5 second responses
- Individual tests validate reasonable times
- Use for performance regression testing

## 🔐 Security Best Practices

### Token Security
- Tokens stored in environment (not visible in requests)
- Automatic token rotation on refresh
- Tokens cleared on account deletion
- No tokens in collection export

### Sensitive Data
- Passwords in environment variables only
- No hardcoded credentials in requests
- Test data clearly marked as test
- Production environment separate

### API Security
- All requests use HTTPS in production
- JWT tokens in Authorization header
- Proper authentication for all protected endpoints
- Validation of all inputs and responses

## 🚀 Advanced Usage

### Collection Runner
Run entire collection automatically:
1. Click **Runner** button
2. Select collection and environment
3. Configure iterations and delays
4. View comprehensive results

### Newman CLI
Run collection from command line:
```bash
# Install Newman
npm install -g newman

# Run collection
newman run LigiOpen-API-Collection.json \
  -e LigiOpen-Development-Environment.json \
  --reporters cli,json \
  --reporter-json-export results.json
```

### CI/CD Integration
Integrate with CI/CD pipelines:
```yaml
# GitHub Actions example
- name: Run API Tests
  run: |
    newman run postman/LigiOpen-API-Collection.json \
      -e postman/LigiOpen-Development-Environment.json \
      --reporters junit \
      --reporter-junit-export test-results.xml
```

## 📞 Support

### Documentation
- **Swagger UI**: http://localhost:8000/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8000/v3/api-docs
- **GitHub Repository**: [Project Repository URL]

### Issues
- Check request documentation in collection
- Verify environment variables
- Review test results and console logs
- Compare with Swagger documentation

### Updates
This collection will be updated as new API endpoints are added:
- Sprint 2: Club Management APIs
- Sprint 3: Player Management APIs  
- Sprint 4: Competition Structure APIs
- Sprint 5: Match Management APIs

---

**Collection Version**: 1.0.0  
**Last Updated**: August 2025  
**Compatible API Version**: v1  
**Maintainer**: LigiOpen Development Team