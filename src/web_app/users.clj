(ns web-app.users)

(use 'web-app.template)
(use 'web-app.mongo)

(use 'hiccup.form)

(require '[noir.session :as session])
(require '[ring.util.response :as response])


(defn users-table [users-fn] 
  [:div.body
   [:h2 "Users"] 
   [:div.form
    
    [:table
     [:tr
      [:th "Name:"]
      [:th "Username:"]
      [:th "Email:"]
      [:th "Action:"]]
     (for [i (users-fn)]
       (identity [:tr
                  [:td (i :name)]
                  [:td (i :user)]
                  [:td (i :email)]
                  [:td 
                   (form-to [:post "/users/delete"]
                            (hidden-field :id (i :_id))
                            (submit-button "Delete"))]]))]]])

(defn users-page [uri]
  (template-page
    "Users page" 
    uri 
    (if (= "admin" (session/get :user)) 
      (users-table get-users)
      [:div.body
       [:div.error "You are not allowed to see this page."]])))

(defn do-delete-user [id]
  (do
    (delete-user id)
    (response/redirect "/users")))