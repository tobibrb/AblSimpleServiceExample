package de.fhb.ablsimpleserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by Tobi on 09.05.2016.
 */
public class SimpleService extends Service implements BeaconConsumer {
    private static final String LOGGER = SimpleService.class.getSimpleName();
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setDebug(true);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(LOGGER, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("THBRanging", null, null, null));
        } catch (RemoteException e) {
            Log.e(LOGGER, "Got Exception: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
