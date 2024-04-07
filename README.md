[![Run Testing](https://github.com/s2g090123/NBAToday/actions/workflows/run_testing.yml/badge.svg)](https://github.com/s2g090123/NBAToday/actions/workflows/run_testing.yml)
[![Danger Checks](https://github.com/s2g090123/NBAToday/actions/workflows/danger_checks.yml/badge.svg)](https://github.com/s2g090123/NBAToday/actions/workflows/danger_checks.yml)
![Coverage](.github/badges/jacoco.svg)

# NBAToday
Note that NBA Today must be used in conjunction with [NBAToday-Server](https://github.com/s2g090123/NBAToday-Server)

## About
With a range of features designed to enhance your NBA experience, this app is perfect for fans who want to stay up-to-date with their favorite teams and players.

**Check your favorite team's schedule**, **view live score updates**, and **check out score box in real-time**. Want to know how your team is performing in the league? Simply **view the standings page to see how they stack up against the competition**.

From points per game to field goal percentage, NBA Today has all the data you need.

Looking for information on individual players? This app includes **personal profiles and career stats for every player** in the league. **Learn more about their background, achievements, and career milestones**.

Customize your experience by **selecting your preferred theme color**, and for those who like to bet on their favorite teams, I've got you covered with the option to **place bets** within the app.

Overall, NBA Today is the perfect companion for basketball fans.

## Technology
[![](https://mermaid.ink/img/pako:eNpdkl9PgzAUxb8KuU-asP8MGh5MdGPLEk3MyHxw9aHCnSOOlpSizm3f3VvQBUd4oOWc3z23twdIVIoQwpsWxdZZ3nPp0HO75hCj_kDNubxaotFqk5lrDi9Op9O5OXJYYlkoWeLF_6MzbQh3RJgKI17Fr0apvO2f7dRnzwpanomtuhUa00eNG9Qok9prZbFRGi8BLe90bTMVqsxIuL8s9JfWuYqrJMGy7EVaK31t6bdFUQePGlBEoFWJE5v7X7uq0skloHevRJrJt3-gWQOaEegpw88HOuDdGRUbYbBnQx2deSOc24oLS5ionDoQr7tWp6uFE32gNMQFF3LUuchSGtjBmjmYLeYUNKTPVOh3DlyeSCcqo-K9TCA0ukIXqiKlutNM0JxzCDdiV553o9Se2Z-yEPKZZtVaQniALwgHLOgG_SHzht7YZ54fuLCn3VGXjRgLPNb3Rmzk-8OTC981oN8NhkHAxp7PBv3BmF4XsK710Ny5-uqdfgAFocmq?type=png)](https://mermaid.live/edit#pako:eNpdkl9PgzAUxb8KuU-asP8MGh5MdGPLEk3MyHxw9aHCnSOOlpSizm3f3VvQBUd4oOWc3z23twdIVIoQwpsWxdZZ3nPp0HO75hCj_kDNubxaotFqk5lrDi9Op9O5OXJYYlkoWeLF_6MzbQh3RJgKI17Fr0apvO2f7dRnzwpanomtuhUa00eNG9Qok9prZbFRGi8BLe90bTMVqsxIuL8s9JfWuYqrJMGy7EVaK31t6bdFUQePGlBEoFWJE5v7X7uq0skloHevRJrJt3-gWQOaEegpw88HOuDdGRUbYbBnQx2deSOc24oLS5ionDoQr7tWp6uFE32gNMQFF3LUuchSGtjBmjmYLeYUNKTPVOh3DlyeSCcqo-K9TCA0ukIXqiKlutNM0JxzCDdiV553o9Se2Z-yEPKZZtVaQniALwgHLOgG_SHzht7YZ54fuLCn3VGXjRgLPNb3Rmzk-8OTC981oN8NhkHAxp7PBv3BmF4XsK710Ny5-uqdfgAFocmq)
[![](https://mermaid.ink/img/pako:eNptk11vgjAYhf-Kea_RKCA0XCyZ4ve34M2KF51000yEAC5zxv--0reYGSFc8Jxz0p6W9gq7OOTgwGfKkn3N7wSnmnhe6ZoncXbI4_SyrdXr9Zda6SB1H8h9oN4D9R9ogNShHZ6rgYdKQpojdemARVwlRkpDWiC51NvteXg-lqmx0pGWSD26PLILT1VmolSkFVKf-pxFKjFVGtIaaUA32X2MmdKQPKRhsSCXxSrjozqSq3iSx_fqT9ZE9X0yprLkkzyjLsuZJ_4T36IyL5p4PP0-7Mqd2aCzkG0qreW9UaW9Uq0qzbVsVml5ct8qLV8Wf2dZ2XtDi5zYZdAg4mnEDqE4ltfCDCDf84gH4IjPkKVfAQSnm8ixcx57l9MOnDw9cw3OSchy7h6YOM0ROB_smN3VXlic5jKZsNNbHEf_EJwr_IDTInbDburE1M22RUzL1uAiVKNBDEJskzRNgxiWpd80-JUDNBu2btukbVqk1Wy1xasBl3PN8GbJC3b7AwRU8aU?type=png)](https://mermaid.live/edit#pako:eNptk11vgjAYhf-Kea_RKCA0XCyZ4ve34M2KF51000yEAC5zxv--0reYGSFc8Jxz0p6W9gq7OOTgwGfKkn3N7wSnmnhe6ZoncXbI4_SyrdXr9Zda6SB1H8h9oN4D9R9ogNShHZ6rgYdKQpojdemARVwlRkpDWiC51NvteXg-lqmx0pGWSD26PLILT1VmolSkFVKf-pxFKjFVGtIaaUA32X2MmdKQPKRhsSCXxSrjozqSq3iSx_fqT9ZE9X0yprLkkzyjLsuZJ_4T36IyL5p4PP0-7Mqd2aCzkG0qreW9UaW9Uq0qzbVsVml5ct8qLV8Wf2dZ2XtDi5zYZdAg4mnEDqE4ltfCDCDf84gH4IjPkKVfAQSnm8ixcx57l9MOnDw9cw3OSchy7h6YOM0ROB_smN3VXlic5jKZsNNbHEf_EJwr_IDTInbDburE1M22RUzL1uAiVKNBDEJskzRNgxiWpd80-JUDNBu2btukbVqk1Wy1xasBl3PN8GbJC3b7AwRU8aU)

Built with the latest in Android app development technologies! Use the **MVVM and Repository Pattern**.

Here are the key technologies I've used to create this app:
- Jetpack Compose
- Coroutine & Coroutine Flow
- Compose Navigation
- Retrofit
- Room
- DataStore
- Koin

## Error handling
[![](https://mermaid.ink/img/pako:eNplkt9vgjAQgP8Vck9bgoZfQsPDEkWWmMwXjHvYuocGTiWTlrRljqn_-yqoI1mf2ut9310vPUIuCoQYtpLVOyt7odwya_pOYYXyCyWFD2s0Gj2dKKRSCmllqGrBFVLKHzLUUmxK_UjhZM16dGbQDGuhSi1kO8ATUVWCW_8t07ruBEkvSIxgrTBh5vaPfm54rssb3xdXopE5duy8Z-eGfS3xsDSP2g9pZLqR2MMPh1LvrCUqxbaGNqr1opOkvSS9NLAY0EWp6j1rr61fwTsANlQoK1YWZozHS4SC3mFlMmKzLZj8pED52eSxRotVy3OItWzQhqYumMZ5ycz0K4g3bK_u0bS4DPCWWTP-JkQ1OEJ8hG-IXRKNI8cjgRdMQhKEkQ2tifpj4hMSBcQJfOKHoXe24acTOOPIiyIyCULiOu7EcUMbsKu17H9C9yHOv-KLp_U?type=png)](https://mermaid.live/edit#pako:eNplkt9vgjAQgP8Vck9bgoZfQsPDEkWWmMwXjHvYuocGTiWTlrRljqn_-yqoI1mf2ut9310vPUIuCoQYtpLVOyt7odwya_pOYYXyCyWFD2s0Gj2dKKRSCmllqGrBFVLKHzLUUmxK_UjhZM16dGbQDGuhSi1kO8ATUVWCW_8t07ruBEkvSIxgrTBh5vaPfm54rssb3xdXopE5duy8Z-eGfS3xsDSP2g9pZLqR2MMPh1LvrCUqxbaGNqr1opOkvSS9NLAY0EWp6j1rr61fwTsANlQoK1YWZozHS4SC3mFlMmKzLZj8pED52eSxRotVy3OItWzQhqYumMZ5ycz0K4g3bK_u0bS4DPCWWTP-JkQ1OEJ8hG-IXRKNI8cjgRdMQhKEkQ2tifpj4hMSBcQJfOKHoXe24acTOOPIiyIyCULiOu7EcUMbsKu17H9C9yHOv-KLp_U)

The flowchart describes the error handling in NBA Today:
1. Server to Repository: The process begins with the server, which, upon encountering an error, sends an "Error Response" using Retrofit
2. Repository to UseCase: Once the repository receives the error response, it processes and wraps it into a "Common Error Response" suitable for the app's internal handling.
3. UseCase to ViewModel: The UseCase further processes the error and translates it into a "Function Error" encapsulated within a Resource.
4. ViewModel to UI: The ViewModel receives the function error and maps it to a "Feature Error (with Message)" suitable for display in the UI and update the state based on the error.
5. UI Handling: Finally, the UI is responsible for displaying the error message to the user.

## CI workflows
NBA Today utilizes two CI workflows: `Danger` and `Testing`.

All generated reports, including Danger, testing result and jacoco code coverage reports, are archived on GitHub for easy access and reference

### Danger workflow
[![](https://mermaid.ink/img/pako:eNp1UU1PwzAM_SuRz23V9StRD0hjZadxgRvtDtZiumhLUqUpMLb9d8IGiMssH2y_p-evI2ysJKihdzhs2eqpMyzYvN35vTJ-zeI4vjuh22zVG51Yc4XvW0medrfgRTs30lklV7clmtbRYJ0ff_AGTU_uxJZXeNkurNZkPEP2js4o0zNN44g9rSECTU6jkmHu4ze_A78lTR3UIZTodh105hx4OHn7fDAbqL2bKIJpkOipURjW1VC_4n78qz5I5a37ZQ5oXqzV_1Koj_AB9UzwhKeZKLKirERR8QgOoZonIheCFyItcpFXVXaO4PMikCY841yURSVm6awMHgFdej1eT3_5wPkL81V7JQ?type=png)](https://mermaid.live/edit#pako:eNp1UU1PwzAM_SuRz23V9StRD0hjZadxgRvtDtZiumhLUqUpMLb9d8IGiMssH2y_p-evI2ysJKihdzhs2eqpMyzYvN35vTJ-zeI4vjuh22zVG51Yc4XvW0medrfgRTs30lklV7clmtbRYJ0ff_AGTU_uxJZXeNkurNZkPEP2js4o0zNN44g9rSECTU6jkmHu4ze_A78lTR3UIZTodh105hx4OHn7fDAbqL2bKIJpkOipURjW1VC_4n78qz5I5a37ZQ5oXqzV_1Koj_AB9UzwhKeZKLKirERR8QgOoZonIheCFyItcpFXVXaO4PMikCY841yURSVm6awMHgFdej1eT3_5wPkL81V7JQ)

The Danger workflow is designed to enforce coding standards and catch potential issues early in the development process.

Danger checks:
1. [ktlint](https://pinterest.github.io/ktlint/latest/): Enforces Kotlin coding style.
2. [detekt](https://detekt.dev/): Performs static code analysis for Kotlin.
3. [Android lint](https://developer.android.com/studio/write/lint): Ensures adherence to Android coding conventions.

Utilize [Danger](https://danger.systems/kotlin/) to comment on PR with any issues found, alerting the PR author or reviewer.

### Testing workflow
[![](https://mermaid.ink/img/pako:eNp9kcFOwzAMhl8l8rkbXdu1UQ9IW9ftAhcGF1oOoTFboE2qLJ0Y696dpINp4oAURc7vz39k-wiV4ggpbDRrt-TuoZSEzIp5J2pOcrkXWskGpXkho9Hotted7Ml8YAaBZC6eF09SGPKIO7P7AZmutmKPPckdkBWZRmbwZm2YNiRvupoZpa9NF45bFDPJtRL8H6-80Ngqfcm-s0pVqidLl1wWGasr547ENWavPWq2wTNMVg5aFSuUVv3LkFfGLQkeNKgbJrgdy9EVlGC22GAJqQ050x8llPJkOdYZtT7IClKjO_Sga7l1XQhmp9lA-sbq3UXNubAt_5Itk89KNVdPSI_wCemEJuPED2gURNOYRnHiwcGq4ZiGlCYR9aOQhnEcnDz4Ggz8cRIkCZ1GMZ34k6k9HuDw1_15s8OCT98tA5ud?type=png)](https://mermaid.live/edit#pako:eNp9kcFOwzAMhl8l8rkbXdu1UQ9IW9ftAhcGF1oOoTFboE2qLJ0Y696dpINp4oAURc7vz39k-wiV4ggpbDRrt-TuoZSEzIp5J2pOcrkXWskGpXkho9Hotted7Ml8YAaBZC6eF09SGPKIO7P7AZmutmKPPckdkBWZRmbwZm2YNiRvupoZpa9NF45bFDPJtRL8H6-80Ngqfcm-s0pVqidLl1wWGasr547ENWavPWq2wTNMVg5aFSuUVv3LkFfGLQkeNKgbJrgdy9EVlGC22GAJqQ050x8llPJkOdYZtT7IClKjO_Sga7l1XQhmp9lA-sbq3UXNubAt_5Itk89KNVdPSI_wCemEJuPED2gURNOYRnHiwcGq4ZiGlCYR9aOQhnEcnDz4Ggz8cRIkCZ1GMZ34k6k9HuDw1_15s8OCT98tA5ud)

The Testing workflow focuses on ensuring the reliability and correctness of the codebase by running both unit and Android tests. Additionally, it generates a code coverage report using [jacoco](https://www.jacoco.org/jacoco/).


## Video
https://github.com/s2g090123/NBAToday/assets/32809761/52b159e3-37bd-432b-b33f-4dac31f00bd6

https://github.com/s2g090123/NBAToday/assets/32809761/421f60f1-ca40-422a-b8bb-249e4375d766

## Screenshot
<p float="left">
<img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705139125.png"  width="200" height="400">
<img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138643.png"  width="200" height="400">
  <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138678.png"  width="200" height="400">
<img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138715.png"  width="200" height="400">
  <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138740.png"  width="200" height="400">
   <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138842.png" width="200" height="400">
   <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138859.png"  width="200" height="400">
   <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138881.png"  width="200" height="400">
  <img src="https://github.com/s2g090123/NBAToday/blob/master/image/Screenshot_1705138983.png"  width="200" height="400">
</p>

## To-do list
- [X] Write Unit Tests
- [X] Write End to End Tests
- [X] CI
- [ ] Deploy NBA Today Server
