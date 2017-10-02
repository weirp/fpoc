{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE DeriveGeneric #-}

module Main where

import Data.Monoid ((<>))
import Data.Aeson (FromJSON, ToJSON)
import GHC.Generics
import Web.Scotty
import Web.Cookie
import Network.HTTP.Types.Status
import Network.Wai.Middleware.RequestLogger (logStdoutDev, logStdout)

import Data.Text.Lazy (Text)
import qualified Data.Text.Lazy as T
import qualified Data.Text.Lazy.Encoding as T
import qualified Data.ByteString as BS
import qualified Data.ByteString.Lazy as BSL
import qualified Blaze.ByteString.Builder as B


data User
  = User {userId :: Int, userName :: String }
  deriving (Show, Generic)
instance ToJSON User
instance FromJSON User

bob :: User
bob = User { userId = 1, userName = "Bob" }

jenny :: User
jenny = User { userId = 2, userName = "Jenny" }

joe :: User
joe = User {userId = 3, userName = "joe@nowhere.com"}

allUsers :: [User]
allUsers = [bob, jenny, joe]

matchesId :: Int -> User -> Bool
matchesId xid user = userId user == xid

passesAuth :: SiteUser -> SiteUser -> Bool
passesAuth credentials user = username credentials == username user && password credentials == password user



data SiteUser
  = SiteUser {username ::String, password :: String, twoFactorAuth :: (Maybe String) }
  deriving (Show, Generic)

instance ToJSON SiteUser
instance FromJSON SiteUser

siteUsers :: [SiteUser]
siteUsers = [
            SiteUser { username = "pw@wc.com", password = "Fr0z3n", twoFactorAuth = Nothing},
            SiteUser { username = "it@wc.com", password = "4dM1n32", twoFactorAuth = Nothing},
            SiteUser {username = "joe@nowhere.com", password = "letmein", twoFactorAuth = Nothing}]

data BankInfo
  = BankInfo {code :: String, countryCode :: String, currency :: String}
  deriving (Show, Generic)
instance ToJSON BankInfo
instance FromJSON BankInfo

thisBank :: BankInfo
thisBank = BankInfo {code = "SAND", countryCode = "PHP", currency = "PHP"}

data Account
  = Account {number :: String, name :: String, balance :: Double}
  deriving (Show, Generic)
instance ToJSON Account
instance FromJSON Account

thisAccount :: Account
thisAccount = Account {number = "12345678", name = "test account", balance = 1054.32}

{-
getCookies :: ActionM (Maybe CookiesText) -}
{-
 getCookies =
    fmap (fmap (parseCookiesText . lazyToStrict . T.encodeUtf8)) $
        reqHeader "Cookie"
    where
        lazyToStrict = BS.concat . BSL.toChunks
-}

makeCookie :: BS.ByteString -> BS.ByteString -> SetCookie
makeCookie n v = def { setCookieName = n, setCookieValue = v }

renderSetCookie' :: SetCookie -> Text
renderSetCookie' = T.decodeUtf8 . B.toLazyByteString . renderSetCookie

setCookie :: BS.ByteString -> BS.ByteString -> ActionM ()
setCookie n v = setHeader "Set-Cookie" (renderSetCookie' (makeCookie n v))


main :: IO ()
main = scotty 3002 $ do
  middleware $ logStdoutDev
  get "/hello/:name" $ do
    name <- param "name"
    text ("hello " <> name <> "!")

  get "/users/:id" $ do
    xid <- param "id"
    json (filter (matchesId xid) allUsers)

  get "/users" $ do
    json allUsers
  get "/json/v1/api/bank" $ do
    json thisBank

{- post here with
curl -v -c - -X POST localhost:3002/json/v1/api/users/authenticate -d '{"username":"xx","password":"xx"}'
-}
  post "/json/v1/api/user/authenticate" $ do

    t <- jsonData
    case filter (passesAuth (t ::SiteUser)) siteUsers of
      [] -> do
        status status400
        text("nah")
      _ -> do
        setCookie "JSESSIONID" "JWIWJSHSHJWJ"
        json (filter (passesAuth (t ::SiteUser)) siteUsers)

  get "/json/v1/api/account" $ do
    json thisAccount

  get "/v1/ api/ integration/ queues" $ do
    {-_
         cookies <- getCookies
         case cookies of
         Just cs -> text ("found some cookies")
         Nothing -> return (400)
      -}
    text("nothing yet")
