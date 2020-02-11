package e.allou.spoilers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Implementation of App Widget functionality.
 */
public class ParameterWidget extends AppWidgetProvider {



    private final String KEY_EMAIL = "email";
    private final String KEY_USERNAME = "username";
    private final String KEY_PREF = "MyPref";
    private final String TAG_WIDGET = "ParameterWidget";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.parameter_widget);
        views.setTextViewText(R.id.appwidget_text, "HELLO");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.parameter_widget);
            views.setTextViewText(R.id.appwidget_text,"HELLO YOU");

            /*
            Intent intent = new Intent(context, ParameterWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            PendingIntent pendingIntent  = PendingIntent.getBroadcast(context,0,
                    intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    */

            Intent intentParametre = new Intent(context,Parametre.class);
            PendingIntent pendingIntentParametre = PendingIntent.getActivity(context, 0, intentParametre, 0);

            Intent intentLogin = new Intent(context,Login.class);
            PendingIntent pendingIntentLogin = PendingIntent.getActivity(context, 0, intentLogin, 0);

            SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PREF,0);

            //TODO : SI LOGOUT, Lancer le Menu + POP UP vous n'etes pas connecté
            String userEmail = "userMail null";

            userEmail = sharedPreferences.getString(KEY_EMAIL,null);
            Log.d(TAG_WIDGET,userEmail+"ok");

           FirebaseUser user = mAuth.getCurrentUser();

            if(user == null) views.setOnClickPendingIntent(R.id.buttonAccesParameter, pendingIntentLogin);

            else views.setOnClickPendingIntent(R.id.buttonAccesParameter, pendingIntentParametre);

            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }



    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

