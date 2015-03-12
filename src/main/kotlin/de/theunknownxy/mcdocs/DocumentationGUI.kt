package de.theunknownxy.mcdocs

import de.theunknownxy.mcdocs.gui.utils.*
import de.theunknownxy.mcdocs.docs.DocumentationBackend

public class DocumentationGUI(val backend: DocumentationBackend) : UnscaledWidgetGui() {
    override fun initGui() {
        root = root(this, {
            hbox {
                spacer { hpolicy { expanding(2f) } }
                treebar {
                    hpolicy { fixed(160f) }
                    backend(backend)
                }
                document {
                    hpolicy { expanding(70f) }
                    backend(backend)
                }
                spacer { hpolicy { expanding(15f) } }
            }
        })
    }
}