package de.fhkiel.rob.legoosctester

import de.fhkiel.rob.legoosctester.gui.Base
import de.fhkiel.rob.legoosctester.osc.OSCReceiver

fun main() {
    Base()

    OSCReceiver.start()
}