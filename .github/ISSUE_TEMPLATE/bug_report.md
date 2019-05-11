---
name: Bug report
about: Create a report to help us improve

---

**CommandAPI version**
The version of the CommandAPI that has caused this bug

**Describe the bug**
A clear and concise description of what the bug is.

**My code**
This code reproduces the behavior:
```java
LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
CommandAPI.getInstance().register("mycmd", args, (sender, args) -> {
    //blah
});
```

**Expected behavior**
A clear and concise description of what you expected to happen.

**Screenshots**
If applicable, add screenshots to help explain your problem.

**Additional context**
Add any other context about the problem here.
