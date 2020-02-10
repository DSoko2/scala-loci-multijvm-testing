ThisBuild / organization := "de.tuda.rpt"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.9"

val scalaLociVersion = "0.3.0"

lazy val lulesh = (project in file("."))
	.settings(
		name := "scala-loci-multijvm-testing",

		/**
		 * ScalaTest Configuration
		 */
		resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases",
		libraryDependencies ++= Seq(
			"org.scalactic" %% "scalactic" % "3.0.8",
			"org.scalatest" %% "scalatest" % "3.0.8" % "test"
		),

		/**
		 * ScalaLoci Configuration
		 */
		addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch),
		resolvers += Resolver.bintrayRepo("stg-tud", "maven"),
		libraryDependencies ++= Seq(
			"de.tuda.stg" %% "scala-loci-lang" % scalaLociVersion,
			"de.tuda.stg" %% "scala-loci-lang-transmitter-rescala" % scalaLociVersion,
			"de.tuda.stg" %% "scala-loci-communicator-tcp" % scalaLociVersion,
			"de.tuda.stg" %% "scala-loci-serializer-upickle" % scalaLociVersion
		)
	)
	.enablePlugins(MultiJvmPlugin)
	.configs(MultiJvm)
