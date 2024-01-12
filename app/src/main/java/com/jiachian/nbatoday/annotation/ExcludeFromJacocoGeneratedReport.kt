package com.jiachian.nbatoday.annotation

/**
 * This annotation is used to mark functions or classes that should be excluded from code coverage analysis
 * performed by Jacoco. It is particularly useful when you want to skip specific parts of your codebase
 * from being included in the coverage report.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class ExcludeFromJacocoGeneratedReport
