DACOM
====

The Clojure community is all about libraries that do one thing and do it well, and justifiably so.
However, it can be especially useful for newcomers to the community to see a more "horizontal" cross-section
of what's out there. To that end, the DACOM ([**Da**tomic](http://datomic.com),
[**C**ompojure](https://github.com/weavejester/compojure), and [**Om**](https://github.com/swannodette/om)) stack is a
whirlwind tour of what I see as some of the most exciting pieces of the Clojure(Script) ecosystem in one rich webapp.
I still have plenty to learn about the world of Clojure, so I welcome feedback and contributions from all.

Of course, this is just one possible combination of technologies and is definitely not a one-size-fits-all stack.
However, any feedback on how to better embody Clojure(Script)/Datomic/Compojure/Om/Leiningen "best practices" are
especially welcome.

##Overview of Technologies Used
This project uses a wide array of Clojure (and non-Clojure) libraries and tools to "glue" everything together and provide some development conveniences, but the main components of the DACOM (rhymes with "datom") stack are:

####Datomic
[Datomic](http://datomic.com) is a database that provides an entity-attribute-value model of data and can be queried with logic-based expressions (it does the equivalent of "joins" and such for you). Time is also a first-class citizen in Datomic. That is, it keeps a complete version history of the entire database, using structural sharing to reduce redundancy (like Clojure's own persistent data structures or the Git internals). You can also grab a given version of the database whenever you want and query (read from) it to your heart's content, knowing that it refers to a single moment in time. Check out [day-of-datomic](https://github.com/Datomic/day-of-datomic) for a great guide to working with Datomic in Clojure.

####Compojure
[Compojure](https://github.com/weavejester/compojure) is a lightweight abstraction on top of  [Ring](https://github.com/ring-clojure/ring) request-handling, which itself is an abstraction on top of Java servlets. Ring is a very streamlined, data-driven library that allows you to define handlers for HTTP requests that can be easily wrapped/extended with a flexible "middleware" model and allows deployment via WARs or standalone, Jetty-based JARs. Compojure adds some additional routing conveniences to let you define the various resources that make up your app kind of like in Ruby on Rails (though a better comparison would be to "micro-frameworks" like [Flask](https://github.com/mitsuhiko/flask) or [Sinatra](https://github.com/sinatra/sinatra)).

####Om
While Datomic are Compojure are fairly stable, [Om](https://github.com/swannodette/om) is really hot-off-the-press (as of January, 2014). However, it has already garnered a great deal of praise from the internet, which is hardly ever wrong. It is based on Facebook's React library, which, compared to many front-end frameworks, generally limits itself to binding data to the DOM. This fits well with the Clojure philosophy of small libraries and data-orientation. You can read more about why Om's author thinks it's cool [here](http://swannodette.github.io/2013/12/17/the-future-of-javascript-mvcs/).

##Quickstart with Datomic Free/Starter
Datomic has a number of editions, offers a number of language interfaces, and supports a number of storage back-ends, so its documentation can be a little overwhelming. However, you can get started with a basic setup by:

* [Getting Datomic](http://www.datomic.com/get-datomic.html) (there are a couple of free options&mdash;this project is setup to use the "free" version)
* Running `bin/transactor config/samples/free-transactor-template.properties` (this works fine as of version 0.9.4384)

##Building and Running
Working with this version of the DACOM stack is a little more cumbersome than it ideally should be, mainly do to a lack of front-end oriented tools in the [Leiningen](http://leiningen.org) ecosystem. There are a few such tools out there, but they are generally simple shell-based wrappers around NodeJS-based tools, so I have instead simply included some convenience tasks to invoke those tools through Leiningen without additional plugins.

####Prerequisites
* [Datomic](http://datomic.com) (any edition)
* [Leiningen](http://leiningen.org)
* [Bower](http://bower.io)
* [Less](http://lesscss.org)
* [Cerebellum](http://en.wikipedia.org/wiki/Cerebellum)

####Install and Run
* Datomic: With a Datomic transactor up and running with local storage (free/dev protocol&mdash;see above), run `lein install-db`
  (in the command line) to install the schema and some data
* Compojure: Run `lein run-server` to run the Ring (and Compojure) server, which provides the web service backend
* Om: Run `lein run-client` to assemble the static assets, including the Om front-end app and start a static web
  server

####Debug, Hack, and Play!
* Run `lein cljsbuild auto` to monitor .cljs files for changes and automatically recompile them
* Run `lein watch-less` to monitor less for changes and recompile
* Run `lein repl` to start the usual Leiningen/nREPL-based REPL
* Then, if you want to, type `(browser-repl)` or just `(br)` into the repl to start an Austin-based browser REPL session. Once you've done that, refresh the webapp and try typing `(js/alert "Hey there!")` from the Clojure REPL
