message "Thanks @#{github.pr_author}!"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "WIP"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Warn to encourage a PR description
warn("Please provide a summary in the PR description to make it easier to review") if github.pr_body.length == 0

# Warn to encourage that labels should have been used on the PR
warn("Please add labels to this PR") if github.pr_labels.empty?

# Detekt output check
detekt_dir = "**/build/reports/detekt/detekt.xml"
Dir[detekt_dir].each do |file_name|
  kotlin_detekt.skip_gradle_task = true
  kotlin_detekt.report_file = file_name
  kotlin_detekt.detekt(inline_mode: true)
end

# Ktlint output check
ktlint_dir = [
  "**/build/reports/ktlint/ktlintAndroidTestSourceSetCheck/ktlintAndroidTestSourceSetCheck.json",
  "**/build/reports/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.json",
  "**/build/reports/ktlint/ktlintTestSourceSetCheck/ktlintTestSourceSetCheck.json"
]
ktlint_dir.each do |dir|
  Dir[dir].each do |file_name|
    ktlint.skip_lint = true
    ktlint.report_file = file_name
    ktlint.lint(inline_mode: true)
  end
end

# Android Lint output check
lint_dir = '**/build/reports/lint-result-debug.xml'
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.report_file = file_name
  android_lint.lint(inline_mode: true)
end
