# Integration Testing Setup for SonarQube Demo

This project demonstrates how to configure integration testing with SonarQube reporting in a Java web application.

## Overview

The integration test framework includes:
- **Maven Failsafe Plugin**: Runs integration tests (files ending with `*IT.java`)
- **JaCoCo Coverage**: Collects coverage from both unit and integration tests
- **REST Assured**: For HTTP endpoint testing
- **Embedded Jetty**: Lightweight server for integration testing

## Test Structure

### Integration Tests Location
- `src/test/java/**/*IT.java` - Integration test files
- Tests are automatically discovered by Maven Failsafe plugin

### Example Tests
1. **HomeServletIT** - Tests the `/helloWorld` endpoint with embedded Jetty server
2. **UserServletIT** - Demonstrates testing with database interactions (mocked for demo)
3. **UtilsIT** - Shows utility class integration testing

## Maven Configuration

### Key Plugins Added:
- `maven-failsafe-plugin` - Runs integration tests
- `maven-surefire-plugin` - Excludes integration tests from unit test phase
- `jacoco-maven-plugin` - Enhanced with integration test coverage

### Coverage Reports Generated:
- `target/site/jacoco/jacoco.xml` - Unit test coverage
- `target/site/jacoco-it/jacoco.xml` - Integration test coverage  
- `target/site/jacoco-merged/jacoco.xml` - Combined coverage report

## Running Tests

### Local Development
```bash
# Run only unit tests
mvn test

# Run only integration tests  
mvn verify -DskipUTs=true

# Run all tests with coverage
mvn verify
```

### CI/CD Pipeline
The GitHub Actions workflow now:
1. Runs unit tests separately
2. Runs integration tests with Failsafe
3. Generates merged coverage reports
4. Sends all test results and coverage to SonarQube

## SonarQube Integration

The SonarQube scanner is configured to include:
- **Test Results**: Both Surefire (unit) and Failsafe (integration) reports
- **Coverage Data**: Merged coverage from unit and integration tests
- **Code Analysis**: Static analysis of both main and test code

### SonarQube Properties:
```
-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml,target/site/jacoco-it/jacoco.xml,target/site/jacoco-merged/jacoco.xml
-Dsonar.junit.reportPaths=target/surefire-reports,target/failsafe-reports
```

## Benefits for Demo

This setup demonstrates:
1. **Separation of Concerns**: Unit vs Integration test execution
2. **Comprehensive Coverage**: Combined reporting from both test types
3. **CI/CD Integration**: Automated execution in GitHub Actions
4. **SonarQube Visibility**: All test metrics visible in SonarQube dashboard

## Next Steps for Production

For a production environment, consider:
- **TestContainers**: For real database integration tests
- **WireMock**: For external service mocking
- **Test Data Management**: Proper test data setup/teardown
- **Performance Testing**: Load and stress test integration
