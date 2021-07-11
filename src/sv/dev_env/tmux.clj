(ns sv.dev-env.tmux
  (:require [babashka.process :as process]))

(defn- shell
  [cmd]
  @(process/process
    [cmd]
    {:out :inherit
     :err :inherit}))

(defn new-window!
  [n name]
  (shell (str "tmux new-window -t dev:"
              n
              " -n '" name "'")))

(defn select-window!
  [n]
  (shell (str "tmux select-window -t dev:"
              n)))

(defn execute!
  [n cmd]
  (shell (str "tmux send-keys -t dev '"
              cmd "' enter")))

(defn start!
  [{:keys [name n cmd]}]
  (new-window! n name)
  (select-window! n)
  (execute! n cmd))


(defn start-all!
  [tasks]
  (doseq [[n task] (map vector
                        (range)
                        tasks)]
    (start! {:n (inc n)
             :name task
             :cmd (str "bb "
                       task)})))

(defn new-session!
  []
  (shell "tmux -2 new-session -d -s dev"))

(defn attach-session!
  []
  (shell "tmux -2 attach-session -t dev"))
