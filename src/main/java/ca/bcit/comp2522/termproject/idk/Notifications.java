package ca.bcit.comp2522.termproject.idk;


import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Add various notifications to user.
 */
    public class Notifications {

        /**
         * Greet user with notification
         */
        protected void notification() {
            onKeyDown(KeyCode.N, "Notify", () -> getNotificationService().pushNotification("Hello Prince"));
        }

}
