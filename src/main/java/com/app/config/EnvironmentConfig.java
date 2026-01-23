package com.app.config;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentConfig {
    private static final Map<String, String> envVars = new HashMap<>();
    
    static {
        // Load environment variables with default values
        envVars.put("DB_URL", getEnvWithDefault("DB_URL", "jdbc:postgresql://localhost:5432/ers_db"));
        envVars.put("DB_USERNAME", getEnvWithDefault("DB_USERNAME", "postgres"));
        envVars.put("DB_PASSWORD", getEnvWithDefault("DB_PASSWORD", ""));
        envVars.put("APP_PORT", getEnvWithDefault("APP_PORT", "8080"));
        envVars.put("APP_CONTEXT", getEnvWithDefault("APP_CONTEXT", "ExpenseReimbursementSystem"));
        envVars.put("LOG_LEVEL", getEnvWithDefault("LOG_LEVEL", "INFO"));
        envVars.put("LOG_FILE_PATH", getEnvWithDefault("LOG_FILE_PATH", "logs/application.log"));
        envVars.put("JSON_PRETTY_PRINT", getEnvWithDefault("JSON_PRETTY_PRINT", "false"));
    }
    
    private static String getEnvWithDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
    
    public static String getDbUrl() {
        return envVars.get("DB_URL");
    }
    
    public static String getDbUsername() {
        return envVars.get("DB_USERNAME");
    }
    
    public static String getDbPassword() {
        return envVars.get("DB_PASSWORD");
    }
    
    public static int getAppPort() {
        return Integer.parseInt(envVars.get("APP_PORT"));
    }
    
    public static String getAppContext() {
        return envVars.get("APP_CONTEXT");
    }
    
    public static String getLogLevel() {
        return envVars.get("LOG_LEVEL");
    }
    
    public static String getLogFilePath() {
        return envVars.get("LOG_FILE_PATH");
    }
    
    public static boolean isJsonPrettyPrint() {
        return Boolean.parseBoolean(envVars.get("JSON_PRETTY_PRINT"));
    }
}