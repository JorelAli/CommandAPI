# Annotation-based commands

The CommandAPI also includes a very small lightweight annotation-based command framework. This works very differently compared to previous commands shown in this documentation and **it is less feature-rich than registering commands using the other methods.**

In short, the CommandAPI's annotation-based system:

- Has no runtime overhead compared to using the regular command registration system (unlike other annotation-based frameworks such as ACF).
- Reduces code bloat (to an extent).
- Improves readability since commands are declared declaratively instead of imperatively.
- Is not as powerful as the regular command registration system.

Currently, the annotation framework is in its infancy, so any suggestions or improvements are heavily appreciated!

-----

Before we go into too much detail, let's take a look at a few examples of what this annotation framework can do, and compare this to the existing method.

```java

```

