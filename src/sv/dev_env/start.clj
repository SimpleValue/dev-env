(ns sv.dev-env.start
  (:require [clojure.java.io :as io]))

(defn start
  [{:keys [image ports env-vars cmd container-name extra-opts]}]
  (concat
   ["docker" "run"
    "-it"
    "-v" (str (.getCanonicalPath (io/file "dev-home"))
              ":/home/dev")
    "-v" (str (.getCanonicalPath (io/file "."))
              ":/home/dev/app")
    "-v" (str (System/getProperty "user.home")
              "/.ssh"
              ":/home/dev/.ssh/")
    "--name" (or container-name
                 "dev")
    "--rm"
    "--ipc=host"
    "--privileged"
    ]
   (mapcat
    (fn [{:keys [host-port container-port]}]
      ["-p" (str host-port ":" container-port)])
    ports)
   (mapcat
    (fn [[k v]]
      ["-e" (str k "=" v)])
    env-vars)
   extra-opts
   [image]
   (or cmd
       ["bb" "dev-tmux"])))
