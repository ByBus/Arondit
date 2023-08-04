package host.capitalquiz.arondit.core.ui

import android.app.Application
import host.capitalquiz.arondit.db.GameDataBase

class App: Application() {
    val db by lazy { GameDataBase.database(this) }
}