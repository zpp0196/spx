package me.zpp0196.spx.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import me.zpp0196.spx.R

/**
 * @author zpp0196
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        setTitle(R.string.activity_settings_title)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> showAboutDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.menu_about)
            setView(R.layout.about_dialog)
            setNegativeButton(android.R.string.ok) { dialog, _ ->
                dialog.cancel()
            }
        }.show()
    }
}