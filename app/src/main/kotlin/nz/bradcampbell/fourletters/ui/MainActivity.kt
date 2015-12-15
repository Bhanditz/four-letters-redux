package nz.bradcampbell.fourletters.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import nz.bradcampbell.fourletters.App
import nz.bradcampbell.fourletters.redux.action.ActionCreator
import nz.bradcampbell.fourletters.redux.state.Page
import nz.bradcampbell.fourletters.redux.state.StateParcel
import nz.bradcampbell.fourletters.redux.store.Store
import rx.Subscription
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var store: Store
    @Inject lateinit var actionCreator: ActionCreator

    var displayedPage: Page? = null
    var storeSubscription: Subscription? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as App
        app.getAppComponent().inject(this)

        storeSubscription = store.asObservable().subscribe({ state ->
            if (displayedPage != state.paginationState.currentPage) {
                val page = state.paginationState.currentPage
                setContentView(page.layoutId)
                displayedPage = page
            }
            val content = findViewById(android.R.id.content) as ViewGroup
            content.dispatchRender(state)
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("store-state", StateParcel.wrap(store.state()))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val wrapped = savedInstanceState?.getParcelable<StateParcel>("store-state")
        val s = wrapped?.contents
        if (s != null) {
            store.load(s)
        }
    }

    override fun onBackPressed() {
        if (store.state().paginationState.history.size == 0) {
            super.onBackPressed()
        } else {
            actionCreator.back()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        storeSubscription?.unsubscribe()
    }
}
