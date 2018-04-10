(ns db
  (:require [clojure.string :refer [split]] ))


(defn read-lines 
  
  "This function reads a file, one line at a time "
  [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (doall  (line-seq rdr)) ))

(defn make-cust-map 
  "Creates a customer map"
  [st]
 
    (reduce conj (map hash-map [:custID :name :address :tel] (split st #"\|")))
  )

(defn make-prod-map 
  "Creates a product map"
  [st]
  
    (reduce conj (map hash-map [:prodID :desc :price] (split st #"\|")))
  )


(defn make-sales-map
  "Creates a sales map"
  [st]
  
 
    (reduce conj (map hash-map [:salesID :custID :prodID :itemCount] (split st #"\|")))
  )

(defn createVector 
  [file]
  (map make-cust-map (read-lines file)))


(defn createProdVector 
  [file]
  (map make-prod-map (read-lines file)))


(defn createSalesVector 
  [file]
  (map make-sales-map (read-lines file)))


(defn readFile
  "This function reads and return contents of a file."
  
  [fileName]
  (if (= fileName "cust.txt") 
  (def custVector (createVector fileName)))
  
  (if (= fileName "prod.txt")
      (def prodVector (createProdVector fileName)))
  
  
  (if (= fileName "sales.txt") 
      (def salesVector (createSalesVector fileName))))

;Read the three files: customer file, product file and sales file.

(defn initializeDB 
  "This function initializes the database"
  []
(println "Database has been initialized!")
(readFile "cust.txt")
(readFile "sales.txt")
(readFile "prod.txt"))


(defn getProductDescription
  "This function returns the description of a product, for a give product ID"
  
  [pID aVector]
  (if(empty? aVector)
     (get(first aVector) :desc)
    (do
                  
             (if(= (get (first aVector) :prodID) pID)
               (get(first aVector) :desc)
               (recur pID (rest aVector) )))
             ))



(defn getCustName
  "Returns the customer name, given customer ID"
  
  [id aVector]
  (if(empty? aVector)
     (get(first aVector) :name)
    (do          
       (if(= (get (first aVector) :custID) id)
               (get(first aVector) :name)
               (recur id (rest aVector) )))))

(defn readProdTable
  
  [aVector]
  (if(empty? aVector)
     (get(first aVector) :custID)
     (do 
  
     
       (println [(get(first aVector) :prodID) (get(first aVector) :desc) (get(first aVector) :price)])
       (recur (rest aVector) ))
       ))


(defn readCustTable
  
  [aVector]
  (if(empty? aVector)
     (get(first aVector) :custID)
     (do 
       (println [(get(first aVector) :custID) (get(first aVector) :name) (get(first aVector) :address) (get(first aVector) :tel)])
       (recur (rest aVector) ))
       ))


(def vector-list [])
"Reads and present the sales table in terms of customer names and product description"

(defn custID-to-custName
  "Returns the customer name, given customer ID"
  
  [aVector]
  (if(empty? aVector)
     (get(first aVector) :custID)
     (do 
      ;(conj vector-list [(get(first aVector) :custID) (getCustName (get(first aVector) :custID) custVector) (getProductDescription (get(first aVector) :prodID) prodVector) (get(first aVector) :itemCount)])
     
       (println [(get(first aVector) :custID) (getCustName (get(first aVector) :custID) custVector) (getProductDescription (get(first aVector) :prodID) prodVector) (get(first aVector) :itemCount)])
       (recur (rest aVector) ))
       ))




(defn sumSales
  "Helper function: Returns the total quantity sold for a given product"
  
  ([type items] (sumSales type items 0))       
  
  ([type items total]              
     (if (empty? items)        
       
       (do

         (print (getProductDescription type prodVector))
         (print " : ")
         (println total)
         (print "  ")
         total )
       (recur type (rest items) (if(=  (get (first items) :prodID) type)
                           (+ (read-string (get (first items) :itemCount)) total) 
                           total  )) )))

(defn getPrice
  "Returns the price of a product from it ID"
  
   [pid aVector]
   
   (if (empty? aVector)
     "1"
     (do       
         (if(= (get (first aVector) :prodID) pid)
           (get(first aVector) :price)
           (recur pid (rest aVector) )))))



(defn custBill
  "Helper function: Returns the total bill (sum of purchases) for a customer"
  
  ([id items] (custBill id items 0))       
  
  ([id items total]              
     (if (empty? items)        
       
       (do

         (print ( getCustName id custVector))
         (print " : $")
         (print total)
          (println " ")
         total )
       (recur id (rest items) (if(=  (get (first items) :custID) id)
                           (+ (* (read-string (get(first items):itemCount)) (read-string (getPrice (get (first items):prodID) prodVector))) total) 
                           total  )))))

(defn getProductID
  "Returns product ID using the product names passed as argument"
  
  [name aVector]
                  
   (if(= (get (first aVector) :desc) name)
     (get(first aVector) :prodID)
     (recur name (rest aVector) )))

(defn getCustID
  "Returns customer's ID using customer name"
  
  [aName aVector]
  (if(empty? aVector)
  "-1"
  (do             
  (if(= (get (first aVector) :name) aName)
    (get(first aVector) :custID)
    (recur aName (rest aVector) ))))
             )



(defn getSumForItem
  "Returns the total quantity sold for a given product"
  
  [name items]
  (sumSales (getProductID name items) salesVector) 
  
  )


(defn getSumForCust
  " Returns the total bill (sum of purchases) for a customer"
  
  [name items]
  (custBill (getCustID name items) salesVector) 
  
  )