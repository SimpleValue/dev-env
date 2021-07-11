(ns sv.dev-env.tmux)

(defn new-window!
  [shell n name]
  (shell (str "tmux new-window -t dev:"
              n
              " -n '" name "'")))

(defn select-window!
  [shell n]
  (shell (str "tmux select-window -t dev:"
              n)))

(defn execute!
  [shell cmd]
  (shell (str "tmux send-keys -t dev '"
              cmd "' enter")))

(defn start!
  [shell {:keys [name n cmd]}]
  (new-window! shell n name)
  (select-window! shell n)
  (execute! shell cmd))

(defn start-all!
  [shell tasks]
  (doseq [[n task] (map vector
                        (range)
                        tasks)]
    (start! shell
            {:n (inc n)
             :name task
             :cmd (str "bb "
                       task)})))

(defn new-session!
  [shell]
  (shell "tmux -2 new-session -d -s dev"))

(defn attach-session!
  [shell]
  (shell "tmux -2 attach-session -t dev"))

(defn tmux!
  [shell {:keys [tasks]}]
  (new-session! shell)

  (start-all! shell
              tasks)

  (select-window! shell
                  1)

  (attach-session! shell)
  )
