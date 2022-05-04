package dev.jorel.commandapi.ktannotations

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import dev.jorel.commandapi.annotations.arguments.Primitive
import kotlin.reflect.KClass

fun KSClassDeclaration.findCompanionObject(): KSClassDeclaration? {
	return this.declarations.find {
		it is KSClassDeclaration && it.isCompanionObject
	} as? KSClassDeclaration
}

fun <T : Annotation> KSAnnotated.annotation(annotationKClass: KClass<T>): T? =
	annotations(annotationKClass).firstOrNull()

fun <T : Annotation> KSAnnotated.getAnnotation(annotationKClass: KClass<T>): T =
	annotation(annotationKClass) ?: throw NoSuchElementException("Sequence is empty.")

@OptIn(KspExperimental::class)
fun <T : Annotation> KSAnnotated.annotations(annotationKClass: KClass<T>): Sequence<T> =
	getAnnotationsByType(annotationKClass)

fun <T : Annotation> KSAnnotated.getKSAnnotationsByType(annotationKClass: KClass<T>): Sequence<KSAnnotation> {
	return this.annotations.filter {
		it.shortName.getShortName() == annotationKClass.simpleName && it.annotationType.resolve().declaration
			.qualifiedName?.asString() == annotationKClass.qualifiedName
	}
}

fun KSAnnotation.asPrimitive(): Primitive {
	return this.annotationType.resolve().declaration.annotation(Primitive::class) as Primitive
}

fun KSValueParameter.getArgumentAnnotation(): Pair<KSAnnotation, Annotation>? {
	for (annotationClass in argumentAnnotations) {
		val ksAnnotation = getKSAnnotationsByType(annotationClass).firstOrNull()
		val annotation = annotation(annotationClass)
		if (ksAnnotation != null && annotation != null) return ksAnnotation to annotation
	}
	return null
}

fun String.quoted() = "\"$this\""
fun Array<String>.quoted() = map { it.quoted() }