## Feature Changes Structure

```text
com.gym
│
├── application
│   └── exception
│       └── AccountLockedException.java      -  user's account is locked many failed login attempts.
│
└── infrastructure
    │
    ├── actuator
    │
    ├── config
    │   ├── CorsConfig.java                  - (CORS); exposes Actuator/Prometheus endpoint on port 9000.
    │   ├── PasswordEncoderConfig.java       - BCryptPasswordEncoder
    │   ├── SecurityConfig.java              - SecurityFilterChain public routes:login,trainee/trainer creation
    │   └── WebConfig.java                   - General web configuration
    │
    ├── metrics
    ├── persistence
    │
    ├── security
    │   ├── GymUserDetailsService.java       - Loads user details
    │   ├── JwtAuthenticationFilter.java     - Validates JWTs and authenticates incoming requests.
    │   ├── JwtService.java                  - Generates, validates, and parses JWT access tokens.
    │   ├── LoginAttemptService.java         - Tracks failed login attempts and locks accounts when limits are exceeded.
    │   └── TokenBlacklistService.java       - Manages (blacklisted) JWTs after logout.
    │
    └── web
        ├── controller
        ├── dto
        │   ├── auth
        │   │   ├── ChangePasswordRequest.java
        │   │   ├── LoginRequest.java
        │   │   └── LoginResponse.java       - Returns the JWT access token after successful authentication.
        │   ├── error
        │   ├── trainee
        │   ├── trainer
        │   └── training
        └── exception
```