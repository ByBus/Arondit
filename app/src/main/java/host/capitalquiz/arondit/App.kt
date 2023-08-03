package host.capitalquiz.arondit

import android.app.Application
import host.capitalquiz.arondit.data.GameDataBase

class App: Application() {
    val db by lazy { GameDataBase.database(this) }
}