package controller

import models.Ship

/**
 * Created by Basti on 04.12.14.
 */
class Game extends ConsoleSession with GameSession {
  initSession(Ship.generateShipSet(), 0)

}
