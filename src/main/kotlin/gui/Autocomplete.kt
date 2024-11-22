package de.fhkiel.rob.legoosctester.gui

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.MenuKeyEvent
import javax.swing.event.MenuKeyListener

class Autocomplete(private val textField: JTextField, val history: MutableSet<String>): DocumentListener, MenuKeyListener {

    var selection = 0
    var suggestions = listOf<String>()

    var menu = buildMenu()

    init {
        textField.document.addDocumentListener(this)
        textField.addKeyListener( object : KeyListener {
            override fun keyTyped(e: KeyEvent) {
                if (e.isControlDown && e.keyChar == ' ') {
                    if(textField.text.isNotBlank()) {
                        suggest()
                    }
                }
            }

            override fun keyPressed(e: KeyEvent) {
                if(menu.isVisible) {
                    when(e.keyCode){
                        40 -> {
                            selection = 0
                            menu.requestFocusInWindow()
                        }
                        38 -> {
                            selection = suggestions.size-1
                            menu.requestFocusInWindow()
                        }
                    }
                }
            }

            override fun keyReleased(e: KeyEvent) {/* not needed */}

        })
    }

    override fun insertUpdate(e: DocumentEvent?) {
        if(textField.text.isNotBlank()) {
            suggest()
        }
    }

    override fun removeUpdate(e: DocumentEvent?) {
        if(textField.text.isNotBlank()) {
            suggest()
        }
    }

    override fun changedUpdate(e: DocumentEvent?) {
        if(textField.text.isNotBlank()) {
            suggest()
        }
    }

    private fun suggest(){
        menu.isVisible = false
        suggestions = history.filter { it.startsWith(textField.text) }.filter { it != textField.text }
        menu = buildMenu()
        if(menu.components.isNotEmpty()) {
            menu.show(textField, 0, textField.height)
            textField.requestFocusInWindow()
        }
    }

    override fun menuKeyTyped(e: MenuKeyEvent) {}
    override fun menuKeyPressed(e: MenuKeyEvent) {
        when(e.keyCode){
            40 -> {
                selection = (selection + 1) % suggestions.size
            }
            38 -> {
                selection = ((selection - 1) + suggestions.size) % suggestions.size
            }
            10 -> {
                textField.text = suggestions[selection]
                menu.isVisible = false
            }
        }
    }
    override fun menuKeyReleased(e: MenuKeyEvent) {}

    private fun buildMenu(): JPopupMenu {
        val menu = JPopupMenu()
        menu.addMenuKeyListener(this)
        for(s in suggestions){
            val suggestion = JMenuItem(s)
            suggestion.addActionListener {
                textField.text = s
            }
            menu.add(suggestion)
        }
        return menu
    }


}