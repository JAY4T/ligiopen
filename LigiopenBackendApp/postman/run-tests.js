/**
 * LigiOpen API Collection Runner Script
 * 
 * This script runs the complete LigiOpen API test suite using Newman.
 * Supports different environments and generates comprehensive reports.
 * 
 * Usage:
 *   node run-tests.js [environment] [options]
 * 
 * Examples:
 *   node run-tests.js development
 *   node run-tests.js production --verbose
 *   node run-tests.js development --export-results
 */

const newman = require('newman');
const fs = require('fs');
const path = require('path');

// Configuration
const config = {
  collection: './LigiOpen-API-Collection.json',
  environments: {
    development: './LigiOpen-Development-Environment.json',
    production: './LigiOpen-Production-Environment.json'
  },
  outputDir: './test-results',
  defaultEnvironment: 'development'
};

// Command line arguments
const args = process.argv.slice(2);
const environment = args[0] || config.defaultEnvironment;
const isVerbose = args.includes('--verbose') || args.includes('-v');
const exportResults = args.includes('--export-results') || args.includes('-e');

// Colors for console output
const colors = {
  green: '\x1b[32m',
  red: '\x1b[31m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  reset: '\x1b[0m',
  bright: '\x1b[1m'
};

function log(message, color = colors.reset) {
  console.log(`${color}${message}${colors.reset}`);
}

function createOutputDir() {
  if (!fs.existsSync(config.outputDir)) {
    fs.mkdirSync(config.outputDir, { recursive: true });
    log(`📁 Created output directory: ${config.outputDir}`, colors.blue);
  }
}

function validateFiles() {
  // Check collection file
  if (!fs.existsSync(config.collection)) {
    log(`❌ Collection file not found: ${config.collection}`, colors.red);
    process.exit(1);
  }

  // Check environment file
  const envFile = config.environments[environment];
  if (!envFile || !fs.existsSync(envFile)) {
    log(`❌ Environment file not found: ${envFile}`, colors.red);
    log(`Available environments: ${Object.keys(config.environments).join(', ')}`, colors.yellow);
    process.exit(1);
  }

  log(`✅ Files validated successfully`, colors.green);
}

function generateTimestamp() {
  return new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5);
}

function runCollection() {
  const timestamp = generateTimestamp();
  const outputFile = path.join(config.outputDir, `test-results-${environment}-${timestamp}.json`);
  const htmlReport = path.join(config.outputDir, `test-report-${environment}-${timestamp}.html`);

  log(`\n🚀 Starting LigiOpen API Test Suite`, colors.bright + colors.blue);
  log(`📊 Environment: ${environment}`, colors.blue);
  log(`📁 Collection: ${config.collection}`, colors.blue);
  log(`🌍 Environment File: ${config.environments[environment]}`, colors.blue);
  
  if (exportResults) {
    log(`📄 Results Export: ${outputFile}`, colors.blue);
    log(`📋 HTML Report: ${htmlReport}`, colors.blue);
  }
  
  log(`\n${'='.repeat(60)}`, colors.yellow);

  const runOptions = {
    collection: config.collection,
    environment: config.environments[environment],
    reporters: ['cli'],
    reporter: {
      cli: {
        verbose: isVerbose,
        showTimestamps: true,
        noAssertions: false,
        noConsole: false
      }
    }
  };

  // Add export options if requested
  if (exportResults) {
    createOutputDir();
    runOptions.reporters.push('json', 'htmlextra');
    runOptions.reporter.json = {
      export: outputFile
    };
    runOptions.reporter.htmlextra = {
      export: htmlReport,
      title: `LigiOpen API Test Report - ${environment.toUpperCase()}`,
      titleSize: 2,
      logs: true,
      skipSensitiveData: true,
      showOnlyFails: false,
      testPaging: true,
      browserTitle: `LigiOpen API Tests - ${environment}`
    };
  }

  // Run the collection
  newman.run(runOptions, function (err, summary) {
    log(`\n${'='.repeat(60)}`, colors.yellow);
    
    if (err) {
      log(`❌ Collection run failed: ${err.message}`, colors.red);
      process.exit(1);
    }

    // Print summary
    const stats = summary.run.stats;
    const failures = summary.run.failures;

    log(`\n📊 Test Results Summary:`, colors.bright + colors.blue);
    log(`   Requests: ${stats.requests.total}`, colors.blue);
    log(`   Tests: ${stats.tests.total}`, colors.blue);
    log(`   Passes: ${stats.tests.total - stats.tests.failed}`, colors.green);
    log(`   Failures: ${stats.tests.failed}`, stats.tests.failed > 0 ? colors.red : colors.green);
    log(`   Assertions: ${stats.assertions.total}`, colors.blue);
    log(`   Skipped: ${stats.assertions.skipped || 0}`, colors.yellow);

    // Print timing information
    if (summary.run.timings) {
      const totalTime = summary.run.timings.completed - summary.run.timings.started;
      log(`   Total Time: ${totalTime}ms`, colors.blue);
      log(`   Average Response Time: ${Math.round(totalTime / stats.requests.total)}ms`, colors.blue);
    }

    // Print failure details
    if (failures && failures.length > 0) {
      log(`\n❌ Failed Tests:`, colors.red);
      failures.forEach((failure, index) => {
        log(`   ${index + 1}. ${failure.source.name || 'Unknown'}`, colors.red);
        if (failure.error) {
          log(`      Error: ${failure.error.message}`, colors.red);
        }
      });
    }

    // Print export information
    if (exportResults) {
      log(`\n📄 Reports Generated:`, colors.green);
      log(`   JSON Report: ${outputFile}`, colors.green);
      log(`   HTML Report: ${htmlReport}`, colors.green);
    }

    // Final status
    if (stats.tests.failed > 0) {
      log(`\n❌ Test suite completed with failures`, colors.red);
      process.exit(1);
    } else {
      log(`\n✅ All tests passed successfully!`, colors.green);
      process.exit(0);
    }
  });
}

// Show help if requested
if (args.includes('--help') || args.includes('-h')) {
  log(`\n🔧 LigiOpen API Test Runner`, colors.bright + colors.blue);
  log(`\nUsage:`, colors.yellow);
  log(`  node run-tests.js [environment] [options]`, colors.blue);
  log(`\nEnvironments:`, colors.yellow);
  Object.keys(config.environments).forEach(env => {
    log(`  ${env}`, colors.blue);
  });
  log(`\nOptions:`, colors.yellow);
  log(`  --verbose, -v      Show detailed test output`, colors.blue);
  log(`  --export-results, -e    Export JSON and HTML reports`, colors.blue);
  log(`  --help, -h         Show this help message`, colors.blue);
  log(`\nExamples:`, colors.yellow);
  log(`  node run-tests.js development`, colors.blue);
  log(`  node run-tests.js production --verbose`, colors.blue);
  log(`  node run-tests.js development --export-results`, colors.blue);
  log(`\n`);
  process.exit(0);
}

// Main execution
try {
  validateFiles();
  runCollection();
} catch (error) {
  log(`❌ Script failed: ${error.message}`, colors.red);
  process.exit(1);
}