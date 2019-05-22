resolvers += "simplemachines releases" at "https://nexus.simplemachines.com.au/content/repositories/public-releases"

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.5.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.7")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.0.0-RC15")
addSbtPlugin("au.com.simplemachines" % "sbt-kafka-compose" % "0.1.0")
