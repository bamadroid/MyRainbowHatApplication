package com.bamadroid.myrainbowhatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.contrib.driver.pwmspeaker.Speaker;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;

import java.io.IOException;

// import the RainbowHat driver

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Gpio mBlueLedGpio;
    private Gpio mGreenLedGpio;
    private Gpio mRedLedGpio;
    private Speaker mSpeaker;
    private ButtonInputDriver mButtonInputDriverA;
    private ButtonInputDriver mButtonInputDriverB;
    private ButtonInputDriver mButtonInputDriverC;
    private Button mbuzzerButton;
    private Button mPowerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mbuzzerButton = (Button) this.findViewById(R.id.firstButton);
        mbuzzerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playSound();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mPowerButton = (Button) findViewById(R.id.powerButton);
        mPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerOff();
            }
        });

        try {

            // configure rainbow hat buttons
            mButtonInputDriverA = RainbowHat.createButtonAInputDriver(
                    KeyEvent.KEYCODE_A
            );
            mButtonInputDriverA.register();

            mButtonInputDriverB = RainbowHat.createButtonBInputDriver(
                    KeyEvent.KEYCODE_B
            );
            mButtonInputDriverB.register();

            mButtonInputDriverC = RainbowHat.createButtonCInputDriver(
                    KeyEvent.KEYCODE_C
            );
            mButtonInputDriverC.register();

            // Light up the Red LED.
            mBlueLedGpio = RainbowHat.openLedBlue();
            mGreenLedGpio = RainbowHat.openLedGreen();
            mRedLedGpio = RainbowHat.openLedRed();

            mSpeaker = RainbowHat.openPiezo();

        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pins", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                try {
                    mRedLedGpio.setValue(true);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Red LED  GPIO value", e);
                }
                break;
            case KeyEvent.KEYCODE_B:
                try {
                    mGreenLedGpio.setValue(true);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Green LED GPIO value", e);
                }
                break;
            case KeyEvent.KEYCODE_C:
                try {
                    mBlueLedGpio.setValue(true);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Blue LED GPIO value", e);
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                try {
                    mRedLedGpio.setValue(false);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Red LED GPIO value", e);
                }
                break;
            case KeyEvent.KEYCODE_B:
                try {
                    mGreenLedGpio.setValue(false);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Green LED GPIO value", e);
                }
                break;
            case KeyEvent.KEYCODE_C:
                try {
                    mBlueLedGpio.setValue(false);
                } catch (IOException e) {
                    Log.e(TAG, "Error updating Blue LED GPIO value", e);
                }
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonInputDriverA != null) {
            mButtonInputDriverA.unregister();
            try {
                mButtonInputDriverA.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                mButtonInputDriverA = null;
            }
        }

        if (mButtonInputDriverB != null) {
            mButtonInputDriverB.unregister();
            try {
                mButtonInputDriverB.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                mButtonInputDriverB = null;
            }
        }

        if (mButtonInputDriverC != null) {
            mButtonInputDriverC.unregister();
            try {
                mButtonInputDriverC.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            } finally {
                mButtonInputDriverC = null;
            }
        }

        if (mBlueLedGpio != null) {
            try {
                mBlueLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing BLUE LED GPIO", e);
            } finally {
                mBlueLedGpio = null;
            }
            mBlueLedGpio = null;
        }

        if (mRedLedGpio != null) {
            try {
                mGreenLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Green LED GPIO", e);
            } finally {
                mGreenLedGpio = null;
            }
            mGreenLedGpio = null;
        }

        if (mRedLedGpio != null) {
            try {
                mRedLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Red LED GPIO", e);
            } finally {
                mRedLedGpio = null;
            }
            mRedLedGpio = null;
        }

        if (mSpeaker != null) {
            try {
                mSpeaker.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Speaker GPIO", e);
            } finally {
                mSpeaker = null;
            }
            mSpeaker = null;
        }
    }

    private void playSound() throws InterruptedException {
        if (mSpeaker != null) {
            try {
                mSpeaker.play(440);
                Thread.sleep(1000);
                mSpeaker.stop();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void powerOff() {
        try {
            Runtime.getRuntime().exec("reboot -p");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
