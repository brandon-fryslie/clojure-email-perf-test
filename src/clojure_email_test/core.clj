(ns clojure-email-test.core
  (:require [clostache.parser :as clostache]
            [postal.core :as postal])
  (:import (java.net InetSocketAddress Socket)
           (javax.mail Session Message Message$RecipientType Transport)
           (javax.mail.internet MimeMessage InternetAddress))
  (:gen-class))

(defmacro dbg [body]
    `(let [x# ~body]
            (println "dbg:" '~body "=" x#)
                 x#))

(def email "bnvmcjfkdj@mailinator.com")
(def email-address (InternetAddress. email))

(defn- render-template [data]
  (clostache/render-resource "template.html" data))

(defn fixed-length-string
  ([] (fixed-length-string 32))
  ([n]
    (let [chars (map char (range 33 127))
          s     (take n (repeatedly #(rand-nth chars)))]
      (reduce str s))))  

; generate 100 people
(defn gen-people [n]
  (repeatedly n (fn [] {:name (fixed-length-string) 
                          :address (fixed-length-string) 
                          :number (fixed-length-string)})))

(defn send-message [body]
  (postal/send-message {:host "localhost"}
                       {:from email
                        :to email
                        :subject "test"
                        :body body}))


(defn prepare-javamail-message [session body]
  (let [message (MimeMessage. session)]
        (.setFrom message email-address)
        (.addRecipient message Message$RecipientType/TO email-address)
        (.setSubject message "test subject")
        (.setText message "test body")
        (.saveChanges message)
        message))

(def rTO (Message$RecipientType/TO))
(def rCC (Message$RecipientType/CC))
(def rBCC (Message$RecipientType/BCC))

(defn prepare-java-email [{from recipients subject body session} data]
  (doto (MimeMessage. session)
    (.setFrom (InternetAddress. from))
    (.setRecipients rTO (into-array (map #(InternetAddress. %) recipients)))
    (.setSubject subject)
    (.setText text)))

(defn prepare-transport-and-session
  
  )

(defn send-messages-javamail [bodies]
  (let [properties (System/getProperties)
        result (.setProperty properties "mail.smtp.host" "localhost")
        socket (Socket.)
        session (Session/getDefaultInstance properties)
        transport (.getTransport session "smtp")]
    (.setSoTimeout socket 10000)
    (.connect socket (InetSocketAddress. "localhost" 25))
    (.connect transport socket)
    (try
      (doseq [body bodies]
        (.sendMessage transport (prepare-javamail-message session body) (into-array InternetAddress [email-address])))
      ;(println "Sent message with JavaMail")
      (catch Exception e (println e)))
    (.close transport)))

(defn -main
  [& args]
  (println "sending message")
  (println (count (repeatedly 10000 #(send-message (render-template {:people (gen-people 1)})))))
  ;(send-messages-javamail (repeatedly 10000 #(render-template {:people (gen-people 1)})))
  )
