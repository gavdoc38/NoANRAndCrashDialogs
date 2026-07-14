-dontwarn io.github.libxposed.annotation.**
-keep,allowoptimization public class * extends io.github.libxposed.api.XposedModule {
    public <init>();
}
-dontwarn androidx.annotation.**
-keep class androidx.annotation.** { *; }
