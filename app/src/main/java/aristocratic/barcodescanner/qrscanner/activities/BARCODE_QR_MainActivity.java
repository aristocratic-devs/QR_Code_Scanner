package aristocratic.barcodescanner.qrscanner.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import aristocratic.barcodescanner.qrscanner.database.BARCODE_QR_Database;
import aristocratic.barcodescanner.qrscanner.model.BARCODE_QR_History;
import aristocratic.barcodescanner.qrscanner.adapter.BARCODE_QR_HistoryAdapter;
import aristocratic.barcodescanner.qrscanner.adapter.BARCODE_QR_MenuAdapter;
import aristocratic.budiyev.android.codescanner.AutoFocusMode;
import aristocratic.budiyev.android.codescanner.CodeScanner;
import aristocratic.budiyev.android.codescanner.DecodeCallback;
import aristocratic.barcodescanner.qrscanner.R;
import aristocratic.budiyev.android.codescanner.CodeScannerView;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class BARCODE_QR_MainActivity extends Activity {

    private CodeScanner mCodeScanner;
    private ListView list_menu, list_history;
    private LinearLayout img_open_web, img_search, img_send_sms, img_send_mms, img_share, img_add_contact;
    ImageView img_flash;
    CheckBox img_beep, img_copy, img_auto_focus, img_touch_focus;

    private TextView txt_content, txt_content_type, txt_content_time, txt_beep, txt_copy_to_clipboard, txt_auto_focus, txt_touch_focus, txt_header;

    private LinearLayout linear_beep, linear_copy, linear_autofocus, linear_touch_focus;
    CodeScannerView scannerView;

    private SharedPreferences sharedPreferences;

    private boolean flash = false;

    Typeface typeface;

    private boolean list_click = false;
    private ImageView img_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.barcode_qr_activity_main);

        if (BARCODE_QR_SplashActivity.mInterstitialAd != null) {
            BARCODE_QR_SplashActivity.mInterstitialAd.show(this);
        }

        img_type = findViewById(R.id.img_type);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        linear_beep = findViewById(R.id.linear_beep);
//        typeface = Typeface.createFromAsset(getAssets(), "RobotoRegular.ttf");
        linear_beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("beep", !sharedPreferences.getBoolean("beep", true));
                editor.commit();
                img_beep.setChecked(sharedPreferences.getBoolean("beep", true));
            }
        });
        linear_copy = findViewById(R.id.linear_copy);
        linear_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("copy", !sharedPreferences.getBoolean("copy", true));
                editor.commit();
                img_copy.setChecked(sharedPreferences.getBoolean("copy", true));
            }
        });
        linear_autofocus = findViewById(R.id.linear_autofocus);
        linear_autofocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("autofocus", !sharedPreferences.getBoolean("autofocus", true));
                editor.commit();
                img_auto_focus.setChecked(sharedPreferences.getBoolean("autofocus", true));
                mCodeScanner.setAutoFocusMode(sharedPreferences.getBoolean("autofocus", true) ? AutoFocusMode.CONTINUOUS : AutoFocusMode.SAFE);
            }
        });
        linear_touch_focus = findViewById(R.id.linear_touch_focus);
        linear_touch_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("touchfocus", !sharedPreferences.getBoolean("touchfocus", true));
                editor.commit();
                img_touch_focus.setChecked(sharedPreferences.getBoolean("touchfocus", true));
                mCodeScanner.setTouchFocusEnabled(sharedPreferences.getBoolean("touchfocus", true));
            }
        });
        txt_content = findViewById(R.id.txt_content);
        txt_content.setTypeface(typeface);
        txt_content_type = findViewById(R.id.txt_content_type);
        txt_content_type.setTypeface(typeface);
        txt_content_time = findViewById(R.id.txt_content_time);
        txt_content_time.setTypeface(typeface);
        txt_copy_to_clipboard = findViewById(R.id.txt_copy_to_clipboard);
        txt_copy_to_clipboard.setTypeface(typeface);
        txt_auto_focus = findViewById(R.id.txt_auto_focus);
        txt_auto_focus.setTypeface(typeface);
        txt_touch_focus = findViewById(R.id.txt_touch_focus);
        txt_touch_focus.setTypeface(typeface);
        txt_beep = findViewById(R.id.txt_beep);
        txt_beep.setTypeface(typeface);
        txt_header = findViewById(R.id.txt_header);
        txt_header.setTypeface(typeface);
        list_history = findViewById(R.id.list_history);
        img_open_web = findViewById(R.id.img_open_web);
        img_beep = findViewById(R.id.img_beep);
        img_copy = findViewById(R.id.img_copy);
        img_search = findViewById(R.id.img_search);
        img_send_sms = findViewById(R.id.img_send_sms);
        img_send_mms = findViewById(R.id.img_send_mms);
        img_auto_focus = findViewById(R.id.img_auto_focus);
        img_share = findViewById(R.id.img_share);
        img_touch_focus = findViewById(R.id.img_touch_focus);
        img_add_contact = findViewById(R.id.img_add_contact);
        list_menu = findViewById(R.id.list_menu);
        list_menu.setAdapter(new BARCODE_QR_MenuAdapter(this));
        scannerView = findViewById(R.id.scanner_view);
        img_flash = findViewById(R.id.img_flash);

        findViewById(R.id.linear_whole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSlidingMenu();
            }
        });
        img_beep.setChecked(sharedPreferences.getBoolean("beep", true));
        img_copy.setChecked(sharedPreferences.getBoolean("copy", true));
        img_auto_focus.setChecked(sharedPreferences.getBoolean("autofocus", true));
        img_touch_focus.setChecked(sharedPreferences.getBoolean("touchfocus", true));

        img_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flash) {
                    flash = true;
                    img_flash.setImageResource(R.drawable.flash_on);
                    mCodeScanner.setFlashEnabled(true);
                } else {
                    img_flash.setImageResource(R.drawable.flash_off);
                    flash = false;
                    mCodeScanner.setFlashEnabled(false);

                }
            }
        });

        list_menu.setOnItemClickListener((parent, view, position, id) -> {
            closeSlidingMenu();
            switch (position) {
                case 0:
                    img_flash.setVisibility(VISIBLE);
                    txt_header.setText("Scan QR");
                    scannerView.setVisibility(VISIBLE);
                    findViewById(R.id.content).setVisibility(GONE);
                    findViewById(R.id.history).setVisibility(GONE);
                    findViewById(R.id.settings).setVisibility(GONE);

                    break;
                case 1:

                    img_flash.setVisibility(INVISIBLE);
                    txt_header.setText("History");
                    scannerView.setVisibility(GONE);
                    findViewById(R.id.content).setVisibility(GONE);
                    findViewById(R.id.history).setVisibility(VISIBLE);
                    findViewById(R.id.settings).setVisibility(GONE);
                    BARCODE_QR_Database database = new BARCODE_QR_Database(getApplicationContext());
                    database.open();


                    list_history.setAdapter(new BARCODE_QR_HistoryAdapter(BARCODE_QR_MainActivity.this, database.getAllHistory()));
                    list_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Calendar calendar = Calendar.getInstance();
                            list_click = true;
                            final BARCODE_QR_History history = (BARCODE_QR_History) list_history.getAdapter().getItem(position);
                            scannerView.setVisibility(GONE);
                            findViewById(R.id.content).setVisibility(VISIBLE);
                            findViewById(R.id.history).setVisibility(GONE);
                            findViewById(R.id.settings).setVisibility(GONE);
                            setContentData(history.getType(), history.getContent());
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy h:mm a");
                            txt_content.setText(history.getContent());
                            txt_content_type.setText(history.getType());
                            calendar.setTimeInMillis(history.getTime());
                            txt_content_time.setText(simpleDateFormat.format(calendar.getTime()));
                            img_share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Text");
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, history.getContent());
                                    startActivity(sharingIntent);
                                }
                            });
                            if (history.getType().equals(ParsedResultType.ADDRESSBOOK.name())) {
                                img_type.setImageResource(R.drawable.contact_130);
                            } else if (history.getType().equals(ParsedResultType.URI.name())) {
                                img_type.setImageResource(R.drawable.weblink_130);
                            } else if (history.getType().equals(ParsedResultType.SMS.name())) {
                                img_type.setImageResource(R.drawable.sms_130);
                            } else {
                                img_type.setImageResource(R.drawable.text_130);
                            }
                        }
                    });
                    database.close();

                    if (BARCODE_QR_SplashActivity.mInterstitialAd != null) {
                        BARCODE_QR_SplashActivity.mInterstitialAd.show(BARCODE_QR_MainActivity.this);
                    }

                    break;
                case 2:
                    img_flash.setVisibility(INVISIBLE);
                    txt_header.setText("Settings");
                    scannerView.setVisibility(GONE);
                    findViewById(R.id.content).setVisibility(GONE);
                    findViewById(R.id.history).setVisibility(GONE);
                    findViewById(R.id.settings).setVisibility(VISIBLE);
                    if (BARCODE_QR_SplashActivity.mInterstitialAd != null) {
                        BARCODE_QR_SplashActivity.mInterstitialAd.show(BARCODE_QR_MainActivity.this);
                    }

                    break;
                case 3:
                    try {
                        String shareBody = "https://play.google.com/store/apps/details?id="
                                + getPackageName();
                        Intent sharingIntent = new Intent(
                                Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                shareBody);
                        startActivity(Intent.createChooser(sharingIntent,
                                "Share via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        startActivity(new Intent(
                                "android.intent.action.VIEW", Uri
                                .parse("market://details?id="
                                        + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id="
                                        + getPackageName())));
                    }
                    break;
                case 5:
                    try {
                        startActivity(new Intent(
                                BARCODE_QR_MainActivity.this, BARCODE_QR_Policy.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
//        getSupportFragmentManager().beginTransaction().add(new BlankFragment(),"First");
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSlidingMenu();
            }
        });
        findViewById(R.id.img_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (findViewById(R.id.linear_options).getVisibility()) {
                    case INVISIBLE:
                        findViewById(R.id.linear_options).animate().translationX(-findViewById(R.id.linear_options).getWidth()).setDuration(0).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                findViewById(R.id.linear_options).animate().translationX(0).setDuration(200).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        findViewById(R.id.linear_options).setVisibility(VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });


                        break;
                    case VISIBLE:
                        findViewById(R.id.linear_options).animate().translationX(-10).setDuration(200).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                findViewById(R.id.linear_options).setVisibility(INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        break;
                }
            }
        });
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setAutoFocusMode(sharedPreferences.getBoolean("autofocus", true) ? AutoFocusMode.CONTINUOUS : AutoFocusMode.SAFE);
        mCodeScanner.setTouchFocusEnabled(sharedPreferences.getBoolean("touchfocus", true));
        scannerView.setFlashButtonVisible(false);
        scannerView.setAutoFocusButtonVisible(false);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                if (flash) {
                    flash = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_flash.setImageResource(R.drawable.flash_off);
                            mCodeScanner.setFlashEnabled(false);

                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_flash.setVisibility(INVISIBLE);
                    }
                });

                final Calendar calendar = Calendar.getInstance();
                final long time = calendar.getTimeInMillis();

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy h:mm a");
                BARCODE_QR_Database database = new BARCODE_QR_Database(getApplicationContext());
                database.open();

                database.insert(new BARCODE_QR_History(ResultParser.parseResult(result).getDisplayResult(), ResultParser.parseResult(result).getType().name(), time));
                database.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scannerView.setVisibility(GONE);
                        img_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Text");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ResultParser.parseResult(result).getDisplayResult());
                                startActivity(sharingIntent);
                            }
                        });
                        findViewById(R.id.content).setVisibility(VISIBLE);
                        findViewById(R.id.history).setVisibility(GONE);
                        findViewById(R.id.settings).setVisibility(GONE);
                        if (sharedPreferences.getBoolean("copy", true)) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("text", ResultParser.parseResult(result).getDisplayResult());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(BARCODE_QR_MainActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();

                        }
                        try {
                            if (sharedPreferences.getBoolean("beep", true)) {
                                playSound(getApplicationContext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//
                        txt_content.setText(ResultParser.parseResult(result).getDisplayResult());
                        txt_content_type.setText(ResultParser.parseResult(result).getType().name());
                        calendar.setTimeInMillis(time);
                        txt_content_time.setText(simpleDateFormat.format(calendar.getTime()));
                        if (ResultParser.parseResult(result).getType().name().equals(ParsedResultType.ADDRESSBOOK.name())) {
                            img_type.setImageResource(R.drawable.contact_130);
                        } else if (ResultParser.parseResult(result).getType().name().equals(ParsedResultType.URI.name())) {
                            img_type.setImageResource(R.drawable.weblink_130);
                        } else if (ResultParser.parseResult(result).getType().name().equals(ParsedResultType.SMS.name())) {
                            img_type.setImageResource(R.drawable.sms_130);
                        } else {
                            img_type.setImageResource(R.drawable.text_130);
                        }
                        setContentData(ResultParser.parseResult(result).getType().name(), ResultParser.parseResult(result).getDisplayResult());
                    }
                });
            }
        });
        if (getIntent().getBooleanExtra("history", false)) {
            img_flash.setVisibility(INVISIBLE);
            txt_header.setText("History");
            scannerView.setVisibility(GONE);
            findViewById(R.id.content).setVisibility(GONE);
            findViewById(R.id.history).setVisibility(VISIBLE);
            findViewById(R.id.settings).setVisibility(GONE);
            BARCODE_QR_Database database = new BARCODE_QR_Database(getApplicationContext());
            database.open();
            list_history.setAdapter(new BARCODE_QR_HistoryAdapter(BARCODE_QR_MainActivity.this, database.getAllHistory()));
            list_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Calendar calendar = Calendar.getInstance();
                    list_click = true;
                    final BARCODE_QR_History history = (BARCODE_QR_History) list_history.getAdapter().getItem(position);
                    scannerView.setVisibility(GONE);
                    findViewById(R.id.content).setVisibility(VISIBLE);
                    findViewById(R.id.history).setVisibility(GONE);
                    findViewById(R.id.settings).setVisibility(GONE);
                    setContentData(history.getType(), history.getContent());
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy h:mm a");
                    txt_content.setText(history.getContent());
                    txt_content_type.setText(history.getType());
                    calendar.setTimeInMillis(history.getTime());
                    txt_content_time.setText(simpleDateFormat.format(calendar.getTime()));
                    img_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Text");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, history.getContent());
                            startActivity(sharingIntent);
                        }
                    });
                    if (history.getType().equals(ParsedResultType.ADDRESSBOOK.name())) {
                        img_type.setImageResource(R.drawable.contact_130);
                    } else if (history.getType().equals(ParsedResultType.URI.name())) {
                        img_type.setImageResource(R.drawable.weblink_130);
                    } else if (history.getType().equals(ParsedResultType.SMS.name())) {
                        img_type.setImageResource(R.drawable.sms_130);
                    } else {
                        img_type.setImageResource(R.drawable.text_130);
                    }
                }
            });
            database.close();
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("history", false)) {
            super.onBackPressed();
            return;
        }
        if (scannerView.getVisibility() == VISIBLE && !list_click) {
            super.onBackPressed();
            return;
        } else {
            txt_header.setText("Scan QR");
            mCodeScanner.startPreview();
            img_flash.setVisibility(VISIBLE);
            scannerView.setVisibility(VISIBLE);
            findViewById(R.id.content).setVisibility(GONE);
            findViewById(R.id.history).setVisibility(GONE);
            findViewById(R.id.settings).setVisibility(GONE);
        }
        if (list_click) {
            txt_header.setText("History");
            list_click = false;
            img_flash.setVisibility(GONE);
            scannerView.setVisibility(GONE);
            findViewById(R.id.content).setVisibility(GONE);
            findViewById(R.id.history).setVisibility(VISIBLE);
            findViewById(R.id.settings).setVisibility(GONE);
        }
        if (findViewById(R.id.linear_options).getVisibility() == VISIBLE) {
            closeSlidingMenu();
        }
    }

    private void setContentData(String type, final String result) {
        if (type.equals(ParsedResultType.ADDRESSBOOK.name())) {
//                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
//                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
////                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mEmailAddress.getText());
//                            intent.putExtra(ContactsContract.Intents.Insert.NAME, ResultParser.parseResult(result).getDisplayResult());
////                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
////                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, mPhoneNumber.getText());
////                            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
//                            startActivity(intent);

            img_search.setVisibility(GONE);
            img_open_web.setVisibility(GONE);
            img_add_contact.setVisibility(VISIBLE);
            img_send_sms.setVisibility(GONE);
            img_send_mms.setVisibility(GONE);
            img_add_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, result);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, result.replaceAll("[^0-9]", ""));
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                    startActivity(intent);
                }
            });
        } else if (type.equals(ParsedResultType.SMS.name())) {

            img_search.setVisibility(GONE);
            img_open_web.setVisibility(GONE);
            img_add_contact.setVisibility(GONE);
            img_send_sms.setVisibility(VISIBLE);
            img_send_mms.setVisibility(VISIBLE);
            img_send_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("smsto:" + result.replaceAll("[^0-9]", ""));
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "The SMS text");
                    startActivity(it);
                }
            });
            img_send_mms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("smsto:" + result.replaceAll("[^0-9]", ""));
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "The SMS text");
                    startActivity(it);
                }
            });
        } else if (type.equals(ParsedResultType.URI.name())) {
            img_search.setVisibility(GONE);
            img_open_web.setVisibility(VISIBLE);
            img_add_contact.setVisibility(GONE);
            img_send_sms.setVisibility(GONE);
            img_send_mms.setVisibility(GONE);
            img_open_web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(result));
                    startActivity(i);
                }
            });
        } else {
            img_search.setVisibility(VISIBLE);
            img_open_web.setVisibility(GONE);
            img_add_contact.setVisibility(GONE);
            img_send_sms.setVisibility(GONE);
            img_send_mms.setVisibility(GONE);
            img_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        String escapedQuery = URLEncoder.encode(result + "", "UTF-8");
//                        Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, result);
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
//                            Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
//                            Matcher m = Patterns.EMAIL_ADDRESS.matcher(ResultParser.parseResult(result).getDisplayResult());
////                            if (m.matches()) {
////
////                            }
//
//                            // if we find a match, get the group
////                            if (m.find())
////                            {
////                                // we're only looking for one group, so get it
////                                String theGroup = m.group(1);
////
////                                // print the group out for verification
////                                System.out.format("'%s'\n", theGroup);
////                                Toast.makeText(DHANVINE_BARCODE_QR_MainActivity.this, theGroup, Toast.LENGTH_SHORT).show();
////                            }
//
//                            String replaced = ResultParser.parseResult(result).getDisplayResult().replaceAll("(?:[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)+", "#");

//                            ResultParser.parseResult(result).getDisplayResult().replaceAll("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+","");
//                            Uri uri = Uri.parse("smsto:" + Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(ResultParser.parseResult(result).getDisplayResult()).);
//                            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//                            it.putExtra("sms_body", "The SMS text");
//                            startActivity(it);
        }
    }

    private void closeSlidingMenu() {
        findViewById(R.id.linear_options).animate().translationX(-findViewById(R.id.linear_options).getWidth()).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                findViewById(R.id.linear_options).setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void playSound(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {

        final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.beep);
        mediaPlayer.start();
//        mediaPlayer.prepareAsync();
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//
//            }
//        });


    }


}
