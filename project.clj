(defproject tic-tac-toe "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC2"]]
  :profiles {:dev {:dependencies
                   [[speclj "3.3.1"]
                    [com.gfredericks/debug-repl "0.0.7"]]}}
  :repl-options {:nrepl-middleware
                  [com.gfredericks.debug-repl/wrap-debug-repl]}
  :plugins [[speclj "3.3.1"]]
  :test-paths ["spec"])
