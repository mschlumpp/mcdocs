package de.theunknownxy.mcdocs

import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.utils.*

public class DocumentationGUI(val backend: DocumentationBackend) : ScaledWidgetGui() {
    override var root: Root? = root(this, {
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