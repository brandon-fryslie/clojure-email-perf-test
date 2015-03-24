(ns clojure-email-test.core
  (:require [clostache.parser :as clostache]
            [postal.core :as postal])
  (:gen-class))

(defn- render-template [data]
  (println "renderin tempalte")
  (clostache/render-resource "template.html" data))

(defn fixed-length-string
  ([] (fixed-length-string 32))
  ([n]
    (let [chars (map char (range 33 127))
          s     (take n (repeatedly #(rand-nth chars)))]
      (reduce str s))))  

; generate 100 people
(defn gen-people []
  (repeatedly 100 (fn [] {:name (fixed-length-string) 
                          :address (fixed-length-string) 
                          :number (fixed-length-string)})))

(defn -main
  [& args]
  (println (render-template {:people (gen-people)}))
  
  )
