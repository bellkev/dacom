DCOM
====

The Clojure community is all about libraries that do one thing and do it well, and justifiably so.
However, it can be especially useful for newcomers to the community to see a more "horizontal" cross-section
of what's out there. To that end, DCOM ([**D**atomic](http://datomic.com),
[**C**ompojure](https://github.com/weavejester/compojure), and [**Om**](https://github.com/swannodette/om)) is a
whirlwind tour of what I see as some of the most exciting pieces of the Clojure(Script) ecosystem in one rich webapp.
I still have plenty to learn about the world of Clojure, so I welcome feedback and contributions from all.

Of course, this is just one possible combination of technologies and is definitely not a one-size-fits-all stack.
However, any feedback on how to better embody Clojure(Script)/Datomic/Compojure/Om/Leiningen "best practices" are
especially welcome.

##Overview of Technologies Used
(coming soon)

##Quickstart with Datomic Free/Starter
(coming soon)

##Building and Running
(Quickie version, more detail to come...)

####Prerequisites
* [Datomic](http://datomic.com) (any edition)
* [Leiningen](http://leiningen.org)
* [Bower](http://bower.io)
* [Less](http://lesscss.org)
* [Cerebellum](http://en.wikipedia.org/wiki/Cerebellum)

####Install and Run
* Datomic: With a Datomic transactor up and running with local storage (free/dev protocol), run `lein install-db`
  (in the command line) to install the schema and some data
* Compojure: Run `lein run-server` to run the Ring (and Compojure) server, which provides the web service backend
* Om: Run `lein run-client` to assemble the static assets, including the Om front-end app and start a static web
  server

####Debug, Hack, and Play!
* Run `lein cljsbuild auto` to monitor .cljs files for changes and automatically recompile them
* Run `lein watch-less` to monitor less for changes and recompile
* Run `lein repl` to start the usual Leiningen/nREPL-based REPL
* Then, if you want to, type `(browser-repl)` or just `(br)` into the repl to start an Austin-based browser REPL session. Once you've done that, refresh the webapp and try typing `(js/alert "Hey there!")` from the Clojure REPL
