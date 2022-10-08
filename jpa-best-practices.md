https://www.jpa-buddy.com/blog/best-practices-and-common-pitfalls/

---

#### 24 June 2021

## Best Practices and Common Pitfalls of Using JPA (Hibernate) with Kotlin

Kotlin is great: it’s more concise and expressive than Java, it allows for safer code and offers seamless
interoperability with Java. The latter allows developers to migrate their projects to Kotlin without having to rewrite
the entire codebase. Such migrations is one of the reasons why we might have to work with JPA in Kotlin. Picking JPA for
a fresh Kotlin application also makes sense, as it is a mature technology familiar to the developers.

There is no JPA without entities, and defining them in Kotlin comes with some caveats. Let’s look at how to avoid the
common pitfalls and make the most of using Kotlin. Spoiler alert: data classes are not the best option for entity
classes.

This article will be mostly focused on Hibernate as it is an undoubtable leader among all JPA implementations.

### Rules for JPA Entities

Entities are not regular DTOs. To work, and work well, they need to satisfy certain requirements, let’s start by
defining them. The JPA Specification provides its own set of restrictions, here are the two most important to us:

1. The entity class must have a no-arg constructor. The entity class may have other constructors as well. The no-arg
   constructor must be public or protected.
2. The entity class must not be final. No methods or persistent instance variables of the entity class may be final.

These requirements are enough to make entities work, but we need additional rules to make them work well:

3. All lazy associations must be loaded only when explicitly requested. Otherwise we might hit unexpected performance
   issues or a LazyInitializationException.
4. equals() and hashCode() implementations must take into account the mutable nature of entities.

### No-arg Constructor

Primary constructors are one of the most loved features in Kotlin. However, adding a primary constructor we lose the
default one, so if you try to use it with Hibernate, you get the following exception:
org.hibernate.InstantiationException: No default constructor for entity . To resolve this issue, you may manually define
a no-args constructor in all entities. Alternatively and preferably, use the kotlin-jpa compiler plugin which ensures
that no-args constructor is generated in the bytecode for each JPA-related class: @Entity, @MappedSuperclass or
@Embeddable.

To enable the plugin, simply add it to the dependencies of kotlin-maven-plugin and to compilerPlugins:

```
<plugin>
   <groupId>org.jetbrains.kotlin</groupId>
   <artifactId>kotlin-maven-plugin</artifactId>
   <configuration>
       <compilerPlugins>
           ...
           <plugin>jpa</plugin>
           ...
       </compilerPlugins>
   </configuration>
   <dependencies>
       ...
       <dependency>
           <groupId>org.jetbrains.kotlin</groupId>
           <artifactId>kotlin-maven-noarg</artifactId>
           <version>${kotlin.version}</version>
       </dependency>
       ...
   </dependencies>
</plugin>
```

In Gradle:

```
plugins {
    id "org.jetbrains.kotlin.plugin.jpa" version "1.5.21"
}
```

### Open Classes and Properties

As per the JPA specification, all JPA-related classes and properties must be open. Some JPA providers don’t enforce this
rule. For example, Hibernate does not throw an exception when it encounters a final entity class. However, a final class
cannot be subclassed, hence the proxying mechanism of Hibernate turns off. No proxies, no lazy loading. Effectively,
this means that all ToOne associations will be always eagerly fetched. This can lead to significant performance issues.
The situation is different for EclipseLink with static weaving, as it doesn’t use subclassing for its lazy loading
mechanism.

Unlike Java, in Kotlin all classes, properties and methods are final by default. You have to explicitly mark them as
open:

```Kotlin
@Table(name = "project")
@Entity
open class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    ...
}
```

Alternatively and preferably, you may use the all-open compiler plugin to make all JPA-related classes and properties
open by default. Make sure to configure it right, so it applies to all classes annotated as @Entity, @MappedSuperclass,
@Embeddable:

```
<plugin>
   <groupId>org.jetbrains.kotlin</groupId>
   <artifactId>kotlin-maven-plugin</artifactId>
   <configuration>
       <compilerPlugins>
           ...
           <plugin>all-open</plugin>
       </compilerPlugins>
       <pluginOptions>
           <option>all-open:annotation=javax.persistence.Entity</option>
           <option>all-open:annotation=javax.persistence.MappedSuperclass</option>
           <option>all-open:annotation=javax.persistence.Embeddable</option>
       </pluginOptions>
   </configuration>
   <dependencies>
       <dependency>
           <groupId>org.jetbrains.kotlin</groupId>
           <artifactId>kotlin-maven-allopen</artifactId>
           <version>${kotlin.version}</version>
       </dependency>
   </dependencies>
</plugin>
```

In Gradle:

```
plugins {
    id "org.jetbrains.kotlin.plugin.allopen" version "1.5.21"
}

allOpen {
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}
```

### Using Data Classes for JPA Entities

Data classes is a great Kotlin feature designed specifically for DTOs. They are final by design and come with default
equals(), hashCode() and toString() implementations, which are very useful. However, these implementations are not well
suited for JPA entities. Let’s see why.

First of all, data classes are final by design and cannot be marked as open in Kotlin. So, the only way to make them
open, hence, applicable for entities, is to enable the all-open compiler plugin.

To examine data classes further, we’ll use the below entity. It has a generated id, a name property and two lazy
OneToMany associations:

```Kotlin
@Table(name = "client")
@Entity
data class Client(
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   var id: Long? = null,

   @Column(name = "name", nullable = false)
   var name: String? = null,

   @OneToMany(mappedBy = "client", orphanRemoval = true)
   var projects: MutableSet<Project> = mutableSetOf(),

   @JoinColumn(name = "client_id")
   @OneToMany
   var contacts: MutableSet<Contact> = mutableSetOf(),
)
```

### Accidentally Fetching LAZY Associations

All ToMany associations are lazy by default for a reason: needlessly loading them can easily harm the performance. A
common case where this could happen is when equals(), hashCode() and toString() implementations use all properties
including LAZY ones. So, calling them results in unwanted requests to the DB or a LazyInitializationException. This is
the default behaviour for data classes: all fields from the primary constructor are used in these methods.

toString() can simply be overridden to exclude all LAZY fields. Make sure not to accidentally add them when using the
IDE to generate toString(). JPA Buddy has its own toString() generation which does not offer LAZY fields as options
altogether.

```Kotlin
@Override
override fun toString(): String {
   return this::class.simpleName + "(id = $id , name = $name )"
}
```

Excluding LAZY fields from equals() and hashCode() is not enough, as they may still contain mutable properties.

### The Problem with Equals() and HashCode()

JPA entities are mutable by their nature, so implementing equals() and hashCode() for them is not as straightforward as
for regular DTOs. Even the id of an entity is often generated by a database, so it gets changed after the entity is
first persisted. This means there are no fields we can rely on to calculate the hashCode.

Let’s run a simple test with the Client entity.

```Kotlin
val awesomeClient = Client(name = "Awesome client")

val hashSet = hashSetOf(awesomeClient)

clientRepository.save(awesomeClient)

assertTrue(awesomeClient in hashSet)
```

The assertion in the last line fails, even though the entity is added to the set just a couple of lines above. Once the
id is generated (on its first save), the hashCode gets changed. So the HashSet looks for the entity in a different
bucket and cannot find it. It wouldn’t be an issue if the id was set during the entity object creation (e.g. was a UUID
set by the app), but DB-generated ids are more common.

To combat this issue, always override equals() and hashCode() when using data classes for entities. How to do it is
explained in great detail
by [Vlad Mihalcea](https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/)
and by [Thorben Janssen](https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/)
. For the Client entity it should look like this:

```Kotlin
override fun equals(other: Any?): Boolean {
   if (this === other) return true
   if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
   other as Client

   return id != null && id == other.id
}

override fun hashCode(): Int = 1756406093
```

### Using an Id Set by the Application

Data class methods are generated using the fields specified in the primary constructor. If it only includes eager
immutable fields, the data class does not have the aforementioned problems. An example of such a field is an immutable
id set by the application:

```kotlin
@Table(name = "contact")
@Entity
data class Contact(
   @Id
   @Column(name = "id", nullable = false)
   val id: UUID,
) {
   @Column(name = "email", nullable = false)
   val email: String? = null

   // other properties omitted
}
```

If you prefer to use a DB-generated id, an immutable natural id can be used in the constructor:

```kotlin
@Table(name = "contact")
@Entity
data class Contact(
   @NaturalId
   @Column(name = "email", nullable = false, updatable = false)
   val email: String
) {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   var id: Long? = null
  
   // other properties omitted
}
```

This is absolutely safe to use. However, it almost defeats the purpose of using data classes, as it makes decomposition
useless and only uses one field in toString(). A plain old class might be a better option for entities.

### Null safety

One of Kotlin’s advantages over Java is a built-in null safety feature. Null safety can also be ensured on the DB side
via non-null constraints. It only makes sense to use these features together.

The simplest way to do this is to define non-null properties in the primary constructor using non-null types:

```kotlin
@Table(name = "contact")
@Entity
class Contact(
   @NaturalId
   @Column(name = "email", nullable = false, updatable = false)
   val email: String,

   @Column(name = "name", nullable = false)
   var name: String

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "client_id", nullable = false)
   var client: Client
) {
   // id and other properties omitted
}
```

However, if you need to exclude them from the constructor (e.g. in a data class), you can either provide a default value
or add the lateinit modifier to the property:

```kotlin
@Entity
data class Contact(
   @NaturalId
   @Column(name = "email", nullable = false, updatable = false)
   val email: String,
) {
   @Column(name = "name", nullable = false)
   var name: String = ""

   @ManyToOne(fetch = FetchType.LAZY, optional = false)
   @JoinColumn(name = "client_id", nullable = false)
   lateinit var client: Client

   // id and other properties omitted
}
```

So, if the property is sure to be not null in the DB, we can also omit all the null checks in the Kotlin code.

### Conclusion

You can find more examples with tests in our [GitHub](https://github.com/jpa-buddy/kotlin-entities) repository. As a
summary of how to define JPA entities in Kotlin, here is a checklist:

Make sure you mark all JPA-related classes and their properties as open to avoid significant performance issues and
enable lazy loading for Many/One to One associations. Or use
the [all-open](https://kotlinlang.org/docs/all-open-plugin.html) compiler plugin and apply it to all classes annotated
as @Entity, @MappedSuperclass and @Embeddable.

Define no-arg constructors in all JPA-related classes or use
the [kotlin-jpa](https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support) compiler plugin, otherwise you’ll get an
InstantiationException.

To use data classes:

Enable the all-open plugin as it was described above, because this is the only way to make data classes open in the
compiled bytecode.

Override equals(), hashCode(), toString() in accordance with one of these articles
by [Vlad Mihalcea](https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/)
or [Thorben Janssen](https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/).

[JPA Buddy](https://plugins.jetbrains.com/plugin/15075-jpa-buddy) is aware of all these things and always generates
valid entities for you, including extra stuff like equals(), hashCode(), toString().


