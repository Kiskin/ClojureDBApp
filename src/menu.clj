;Kisife Giles
;ID: 40001926
(ns menu
	(:require [db :as db]))


(defn display-menu
  "This function displays user menu."
  []
  (println " ")
  
	(println "*****  Sales Menu Options******")
 (println "--------------------------------- ")
	(println "| 1. Display Customer Table  |")
	(println "| 2. Display Product Table |")
  (println "| 3. Display Sales Table|")
  (println "| 4. Total Sales for Customer|")
  (println "| 5. Total Count for Product |")
  (println "| 6. Exit |")
  (println "   ")
  (println " Enter an option?  "))




(defn enterQuery 
  "This fucntion takes as input a enter number and determines the curresponding function to call"
  []
  (println" ")
  
  (println "Please enter a query number -> 1,2 ...6")
  
  (def query (read-line))
   (if (= query "1") 
     (do (println " ")
       (println "Query 1: Customer Table")
       (println " " )
       (db/readCustTable db/custVector) ))
   
    (if (= query "2")
      (do 
        (println " ")
        (println "Query 2: Products Table")
        (println " " )
        (db/readProdTable db/prodVector)))
    
     (if (= query "3") 
       (do
           (println " " )
           (println "Query 3: Sales Table")
      (println " " )
     (db/custID-to-custName db/salesVector)))
     
     (if (= query "4") 
       (do
           (println " " )
           (println "Query 4: Total Sales for Customer") 
     (println "Enter Customer name: ")
     (def customerName (read-line))
     (db/getSumForCust customerName db/custVector)
           ))
     
     (if (= query "5") 
       (do
           (println " " )
           
           (println "Query 5: Total Count for Product")
           (println "Enter item name: ")
           (def itemName (read-line))
           (db/getSumForItem itemName db/prodVector)
           )) 
     
     (if (= query "6") 
       (do
           (println " " )
           (println "Thanks for using my service. Goodbye!!!")
           (. System exit 0))))


(db/initializeDB)

;The code continuously display user menu for new queries.
(while(= 1 1)
( do 
     (display-menu)  
       (enterQuery) )
       )