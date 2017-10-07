(ns translations.es (:require fulcro.i18n #?(:cljs cljs.loader)))

;; This file was generated by Fulcro.

(def
 translations
 {"|Log out" "Cerrar la sesión",
  "|Please supply your name.`" "Por favor suministre su nombre",
  "|Glad you could join us!"
  "Me alegro de que usted podría unirse a nosotros!",
  "|Password" "Contraseña",
  "|Pay Someone page" "Pay Someone page",
  "|Pay Someone" "Pay Someone",
  "|Account Limits page" "Account Limits page",
  "|Password must be at least 7 characters long"
  "La contraseña debe contener al menos 7 caracteres",
  "|Account Number" "Account Number",
  "|Welcome!" "¡Bienvenido!",
  "|International" "International",
  "|Profile" "Profile",
  "|Account to Account Transfer" "Account to Account Transfer",
  "|Account" "Account",
  "|" "",
  "|Email Address" "Dirección de correo electrónico",
  "|Thanks!" "¡Gracias!",
  "|Passwords must match" "Las contraseñas deben coincidir",
  "|Sign in" "Autenticarse",
  "|Please, log in." "Por favor, inicia sesión.",
  "|Welcome! Your account has been created. "
  "¡Bienvenido! Tu cuenta ha sido creada.",
  "|Email" "Email",
  "|Must be a valid email address.`"
  "Debe ser una dirección de correo electrónico válida",
  "|Unable to contact server. Try again later."
  "Incapaz de contactar con el servidor. Vuelva a intentarlo más tarde.",
  "|Don't have a login yet? " "¿Todavía no tienes una cuenta?",
  "|A server error happened. Please try again."
  "Ocurrió un error de servidor. Por favor, inténtelo de nuevo.",
  "|Bad username or password." "Bad username or password.",
  "|Sign Up!" "¡Registrarse!",
  "|Local" "Local",
  "|Main page" "Página Principal",
  "|Send Money Abroad" "Send Money Abroad",
  "|Profile page" "Página de preferencias",
  "|Sign up!" "¡Regístrate!",
  "|Preferences page" "Página de preferencias",
  "|Name" "Nombre",
  "|Verify Password" "Verificar Contraseña",
  "|Balance" "Balance",
  "|Account Limits" "Account Limits"})

(swap! fulcro.i18n/*loaded-translations* assoc "es" translations)