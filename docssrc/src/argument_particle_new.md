# Particle data (1.20.5+)

The particle argument requires additional data for a particle depending on what the particle is. The following particles have additional data required to display them:

<!-- To whoever has to maintain this block, I am sorry! - Skepter -->

<table class="table-wrapper">
    <thead>
        <tr>
            <th>Bukkit Particle</th>
            <th>Argument syntax</th>
        </tr>
    </thead>
    <tr>
        <td><code>BLOCK</code></td>
        <td>
            <pre>block{block_state:{Name:<b>"block_name"</b>}}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>block_name</code></b> - name of a block, such as <code>diamond_block</code></li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>BLOCK_MARKER</code></td>
        <td>
            <pre>block_marker{block_state:{Name:<b>"block_name"</b>}}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>block_name</code></b> - name of a block, such as <code>diamond_block</code></li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>DUST</code></td>
        <td>
            <pre>dust{color:[<b>red</b>,<b>green</b>,<b>blue</b>],scale:<b>scale</b>}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>red</code></b> - number for red, between 0.0 and 1.0</li>
                <li><b><code>green</code></b> - number for green, between 0.0 and 1.0</li>
                <li><b><code>blue</code></b> - number for blue, between 0.0 and 1.0</li>
                <li><b><code>scale</code></b> - number for the size of the particle</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>DUST_COLOR_TRANSITION</code></td>
        <td>
            <pre>dust_color_transition{from_color:[<b>red</b>,<b>green</b>,<b>blue</b>],<br>scale:<b>scale</b>,to_color:[<b>red</b>,<b>green</b>,<b>blue</b>]}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>red</code></b> - number for red, between 0.0 and 1.0</li>
                <li><b><code>green</code></b> - number for green, between 0.0 and 1.0</li>
                <li><b><code>blue</code></b> - number for blue, between 0.0 and 1.0</li>
                <li><b><code>scale</code></b> - number for the size of the particle</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>DUST_PILLAR</code></td>
        <td>
            <pre>dust_pillar{block_state:{Name:<b>"block_name"</b>}}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>block_name</code></b> - name of a block, such as <code>diamond_block</code></li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>ENTITY_EFFECT</code></td>
        <td>
            <pre>entity_effect{color:[<b>red</b>,<b>green</b>,<b>blue</b>,<b>alpha</b>]}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>red</code></b> - number for red, between 0.0 and 1.0</li>
                <li><b><code>green</code></b> - number for green, between 0.0 and 1.0</li>
                <li><b><code>blue</code></b> - number for blue, between 0.0 and 1.0</li>
                <li><b><code>alpha</code></b> - number for transparency, between 0.0 and 1.0</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>FALLING_DUST</code></td>
        <td>
            <pre>falling_dust{block_state:{Name:<b>"block_name"</b>}}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>block_name</code></b> - name of a block, such as <code>diamond_block</code></li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>ITEM</code></td>
        <td>
            <pre>item{item:"<b>item</b>"}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>item</code></b> - name of an item, such as <code>apple</code></li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>SCULK_CHARGE</code></td>
        <td>
            <pre>sculk_charge{roll:<b>angle</b>}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>angle</code></b> - decimal angle the particle displays at in radians</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>SHRIEK</code></td>
        <td>
            <pre>shriek{delay:<b>delay</b>}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>delay</code></b> - delay in ticks for when the shriek particle should appear</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td><code>VIBRATION</code></td>
        <td>
            <pre>vibration{destination:{type:"block",pos:[<b>x</b>,<b>y</b>,<b>z</b>]},<br>arrival_in_ticks:<b>ticks</b>}</pre>
            <ul style="padding-left: 1.5em;">
                <li><b><code>x</code></b> - decimal x-coordinate to move towards</li>
                <li><b><code>y</code></b> - decimal y-coordinate to move towards</li>
                <li><b><code>z</code></b> - decimal z-coordinate to move towards</li>
                <li><b><code>ticks</code></b> - time in ticks to take to move towards its destination</li>
            </ul>
        </td>
    </tr>
</table>

## ParticleArgument examples

Because certain particles (in the table above) require additional data, it is not recommended to spawn a particle without its corresponding data. This can result in particles not showing due to missing requirements.

<div class="warning">

### Example - Show particles at a player's location (without data)

Say we wanted to have a command that displayed particles at a player's location. We will use the following command syntax:

```mccmd
/showparticle <particle>
```

With this, we can simply spawn the particle using the `World.spawnParticle(Particle, Location, int)` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentParticle1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentParticle1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentParticle1}}
```

</div>

Running this can result in errors due to missing requirements. If you provide a particle that has additional requirements, Bukkit will throw an error and the particle will not be displayed. Instead, the example below should be used.

</div>

<div class="example">

### Example - Show particles  at a player's location (with data)

We can fix the issues with the example above by providing the data of the argument using the `ParticleData` record:

```mccmd
/showparticle <particle>
```

In this case, we'll use the `World.spawnParticle(Particle particle, Location location, int count, T data)` method which accepts some particle data:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentParticle2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentParticle2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentParticle2}}
```

</div>

This can be used with commands such as:

```mccmd
/showparticle minecraft:dust_color_transition{from_color:[0.0,0.0,0.0],scale:20.0,to_color:[1.0,0.0,0.0]}
/showparticle minecraft:block_marker{block_state:{Name:"diamond_block"}}
```

</div>

## Particle data implementation notes

The `vibration` particle will return a particle data of the Bukkit `Vibration` class. In the `Vibration` class, you can access the destination location using the `Vibration.getDestination()` method, which returns a `Vibration.Destination` instance. The CommandAPI will **always** return a `Vibration.Destination.BlockDestination` instance, and will never return a `Vibration.Destination.EntityDestination` instance. An example of accessing the location can be found below:

```java
ParticleData<Vibration> particleData; // The particle data you get from your argument
Location destination = ((BlockDestination) particleData.data().getDestination()).getLocation();
```
