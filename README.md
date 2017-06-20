# integrant-example

Example of using integrant with the reloaded workflow

## Usage

```clojure
;; switch to dev ns
user=> (dev)

;; loads the above config into integrant.repl.state/config
dev=> (prep)

;; initialise the system into integrant.repl.state/system
dev=> (init)

;; go = prep + init
dev=> (go)

;; reload source files and restart system
dev=> (reset)

dev=> (halt)
```

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
