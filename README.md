scala-dwolla-hello-world
========================

simple Scala/Lift app demonstrating use of Dwolla's REST API

Prerequisites
==============

The only prerequisite is to have Java 1.5 or later installed.

Installation
============
1. Retrieve your Dwolla app's client key and secret (create an app on Dwolla if you don't already have one) and store them as environment variables - they will be used by the app to authenticate with Dwolla.  Set the following environment variables:
	> DWOLLA_CLIENT_KEY=[INSERT CLIENT KEY HERE]
	> DWOLLA_SECRET=[INSERT SECRET HERE]
2. Run ./sbt if you're using Max or Linux, or sbt.bat if you're using Windows.  Simple Built Tool will automatically download and install any needed dependencies (including Scala).
3. At the SBT prompt (>) type: container:start, wait a few seconds for everything to compile.
4. That's it!  Visit http://127.0.0.1:8080/

