package com.highmobility.hmkit;

import android.graphics.Color;

import com.highmobility.hmkit.Command.AutoHvacState;
import com.highmobility.hmkit.Command.Command;
import com.highmobility.hmkit.Command.CommandParseException;
import com.highmobility.hmkit.Command.Constants;
import com.highmobility.hmkit.Command.DoorLockState;
import com.highmobility.hmkit.Command.Incoming.ChargeState;
import com.highmobility.hmkit.Command.Incoming.ClimateState;
import com.highmobility.hmkit.Command.Incoming.ControlMode;
import com.highmobility.hmkit.Command.Incoming.DeliveredParcels;
import com.highmobility.hmkit.Command.Incoming.Diagnostics;
import com.highmobility.hmkit.Command.Incoming.IgnitionState;
import com.highmobility.hmkit.Command.Incoming.Failure;
import com.highmobility.hmkit.Command.Incoming.Lights;
import com.highmobility.hmkit.Command.Incoming.LockState;
import com.highmobility.hmkit.Command.Incoming.Maintenance;
import com.highmobility.hmkit.Command.Incoming.RooftopState;
import com.highmobility.hmkit.Command.Incoming.TrunkState;
import com.highmobility.hmkit.Command.Incoming.ValetMode;
import com.highmobility.hmkit.Command.Incoming.VehicleLocation;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ttiganik on 15/09/16.
 */
public class IncomingCommand {
    @Test
    public void deliveredParcels_init() {
        /*
        0x00, 0x32, # MSB, LSB Message Identifier for Delivered Parcels
        0x01,       # Message Type for Delivered Parcels
        0x02,       # Two parcels in the car
        0x4B87EFA8B4A6EC08, # Tracking number for first parcel
        0x4B87EFA8B4A6EC09  # Tracking number for second parcel
         */
        byte[] bytes = ByteUtils.bytesFromHex("003201024B87EFA8B4A6EC084B87EFA8B4A6EC09");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == DeliveredParcels.class);
        assertTrue(((DeliveredParcels)command).getDeliveredParcels().length == 2);
        assertTrue(((DeliveredParcels)command).getDeliveredParcels()[0].equals("4B87EFA8B4A6EC08"));
        assertTrue(((DeliveredParcels)command).getDeliveredParcels()[1].equals("4B87EFA8B4A6EC09"));
    }

    @Test
    public void failure_wakeup() {
        byte[] bytes = ByteUtils.bytesFromHex("00020100210001");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == Failure.class);
        assertTrue(((Failure)command).getFailedType() == Command.TrunkAccess.GET_TRUNK_STATE);
        assertTrue(((Failure)command).getFailureReason() == Failure.Reason.UNAUTHORIZED);
    }


    @Test
    public void failure_vs() {
        byte[] bytes = ByteUtils.bytesFromHex("00020100100101");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == Failure.class);
        assertTrue(command.is(Command.FailureMessage.FAILURE_MESSAGE));
        assertTrue(((Failure)command).getFailedType() == Command.Capabilities.CAPABILITIES);
        assertTrue(((Failure)command).getFailureReason() == Failure.Reason.UNAUTHORIZED);
    }

    @Test
    public void controlMode_init() {
        byte[] bytes = ByteUtils.bytesFromHex("002701020032");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == ControlMode.class);
        assertTrue(((ControlMode)command).getMode() == ControlMode.Mode.STARTED);
        assertTrue(((ControlMode)command).getAngle() == 50);
    }

    @Test
    public void lockstate_init() {
        byte[] bytes = ByteUtils.bytesFromHex("00200104000100010000020001030001");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == LockState.class);

        assertTrue(((LockState)command).getFrontLeft() != null);
        assertTrue(((LockState)command).getFrontRight() != null);
        assertTrue(((LockState)command).getRearLeft() != null);
        assertTrue(((LockState)command).getRearRight() != null);

        assertTrue(((LockState)command).getFrontLeft().getPosition() == DoorLockState.DoorPosition.OPEN);
        assertTrue(((LockState)command).getFrontLeft().getLockState() == DoorLockState.LockState.UNLOCKED);

        assertTrue(((LockState)command).getFrontRight().getPosition() == DoorLockState.DoorPosition.CLOSED);
        assertTrue(((LockState)command).getFrontRight().getLockState() == DoorLockState.LockState.UNLOCKED);

        assertTrue(((LockState)command).getRearLeft().getPosition() == DoorLockState.DoorPosition.CLOSED);
        assertTrue(((LockState)command).getRearLeft().getLockState() == DoorLockState.LockState.LOCKED);

        assertTrue(((LockState)command).getRearRight().getPosition() == DoorLockState.DoorPosition.CLOSED);
        assertTrue(((LockState)command).getRearRight().getLockState() == DoorLockState.LockState.LOCKED);
    }

    @Test
    public void lockstate_two_front_doors() {
        byte[] bytes = ByteUtils.bytesFromHex("00200102000100010000");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == LockState.class);

        assertTrue(((LockState)command).getFrontLeft() != null);
        assertTrue(((LockState)command).getFrontRight() != null);
        assertTrue(((LockState)command).getRearLeft() == null);
        assertTrue(((LockState)command).getRearRight() == null);

        assertTrue(((LockState)command).getFrontLeft().getPosition() == DoorLockState.DoorPosition.OPEN);
        assertTrue(((LockState)command).getFrontLeft().getLockState() == DoorLockState.LockState.UNLOCKED);

        assertTrue(((LockState)command).getFrontRight().getPosition() == DoorLockState.DoorPosition.CLOSED);
        assertTrue(((LockState)command).getFrontRight().getLockState() == DoorLockState.LockState.UNLOCKED);
    }

    @Test
    public void rooftopState_init_random() {
        byte[] bytes = ByteUtils.bytesFromHex("0025010135");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == RooftopState.class);
        assertTrue(((RooftopState)command).getDimmingPercentage() == .01f);
        assertTrue(((RooftopState)command).getOpenPercentage() == .53f);
    }

    @Test
    public void rooftopState_init_opaque() {
        byte[] bytes = ByteUtils.bytesFromHex("0025016400");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == RooftopState.class);
        assertTrue(((RooftopState)command).getDimmingPercentage() == 1f);
        assertTrue(((RooftopState)command).getOpenPercentage() == 0f);
    }

    @Test
    public void trunkState_init() {
        byte[] bytes = ByteUtils.bytesFromHex("0021010001");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == TrunkState.class);
        assertTrue(((TrunkState)command).getLockState() == Constants.TrunkLockState.UNLOCKED);
        assertTrue(((TrunkState)command).getPosition() == Constants.TrunkPosition.OPEN);
    }


    @Test
    public void charging_init() {
        byte[] bytes = ByteUtils.bytesFromHex("0023010200FF32BF19999A01905A003C3F5EB85201");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == ChargeState.class);
        assertTrue(((ChargeState)command).getChargingState() == Constants.ChargingState.CHARGING);
        assertTrue(((ChargeState)command).getEstimatedRange() == 255f);
        assertTrue(((ChargeState)command).getBatteryLevel() == .5f);
        assertTrue(((ChargeState)command).getChargerVoltage() == 400f);
        assertTrue(((ChargeState)command).getChargeLimit() == .9f);
        assertTrue(((ChargeState)command).getTimeToCompleteCharge() == 60f);
        assertTrue(((ChargeState)command).getChargingRate() == .87f);
        assertTrue(((ChargeState)command).getBatteryCurrent() == -.6f);
        assertTrue(((ChargeState)command).getChargePortState() == Constants.ChargePortState.OPEN);
    }

    @Test
    public void climate() {
        byte[] bytes = ByteUtils.bytesFromHex("002401419800004140000041ac000041ac000001000041ac00006000000000000000000000071E071F");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == ClimateState.class);
        assertTrue(((ClimateState)command).getInsideTemperature() == 19f);
        assertTrue(((ClimateState)command).getOutsideTemperature() == 12f);
        assertTrue(((ClimateState)command).getDriverTemperatureSetting() == 21.5f);
        assertTrue(((ClimateState)command).getPassengerTemperatureSetting() == 21.5f);

        assertTrue(((ClimateState)command).isHvacActive() == true);
        assertTrue(((ClimateState)command).isDefoggingActive() == false);
        assertTrue(((ClimateState)command).isDefrostingActive() == false);
        assertTrue(((ClimateState)command).getDefrostingTemperature() == 21.5f);

        assertTrue(((ClimateState)command).isAutoHvacConstant() == false);
        AutoHvacState[] autoHvacStates = ((ClimateState)command).getAutoHvacStates();
        assertTrue(autoHvacStates != null);
        assertTrue(autoHvacStates.length == 7);

        assertTrue(autoHvacStates[0].isActive() == false);

        assertTrue(autoHvacStates[5].isActive() == true);
        assertTrue(autoHvacStates[5].getDay() == 5);
        assertTrue(autoHvacStates[5].getStartHour() == 7);
        assertTrue(autoHvacStates[5].getStartMinute() == 30);

        assertTrue(autoHvacStates[6].isActive() == true);
        assertTrue(autoHvacStates[6].getDay() == 6);
        assertTrue(autoHvacStates[6].getStartHour() == 7);
        assertTrue(autoHvacStates[6].getStartMinute() == 31);
    }

    @Test
    public void valetMode() {
        byte[] bytes = ByteUtils.bytesFromHex("00280101");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == ValetMode.class);
        assertTrue(((ValetMode)command).isActive() == true);
    }

    @Test
    public void location() {
        byte[] bytes = ByteUtils.bytesFromHex("0030014252147d41567ab1");
        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == VehicleLocation.class);
        assertTrue(((VehicleLocation)command).getLatitude() == 52.520008f);
        assertTrue(((VehicleLocation)command).getLongitude() == 13.404954f);
    }

    @Test
    public void diagnostics() {
        byte[] bytes = ByteUtils.bytesFromHex("0033010249F00063003C09C45A0104004013d70a014013d70a02401666660340166666");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == Diagnostics.class);
        assertTrue(((Diagnostics)command).getMileage() == 150000);
        assertTrue(((Diagnostics)command).getOilTemperature() == 99);
        assertTrue(((Diagnostics)command).getSpeed() == 60);
        assertTrue(((Diagnostics)command).getRpm() == 2500);
        assertTrue(((Diagnostics)command).getFuelLevel() == .9f);
        assertTrue(((Diagnostics)command).getWasherFluidLevel() == Constants.WasherFluidLevel.FULL);
        assertTrue(((Diagnostics)command).getFrontLeftTirePressure() == 2.31f);
        assertTrue(((Diagnostics)command).getFrontRightTirePressure() == 2.31f);
        assertTrue(((Diagnostics)command).getRearLeftTirePressure() == 2.35f);
        assertTrue(((Diagnostics)command).getRearRightTirePressure() == 2.35f);
    }

    @Test
    public void maintenance() {
        byte[] bytes = ByteUtils.bytesFromHex("00340101F5000E61");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == Maintenance.class);
        assertTrue(((Maintenance)command).getDaysToNextService() == 501);
        assertTrue(((Maintenance)command).getKilometersToNextService() == 3681);
    }

    @Test
    public void engine() {
        byte[] bytes = ByteUtils.bytesFromHex("00350101");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == IgnitionState.class);
        assertTrue(((IgnitionState)command).isOn() == true);
    }

    @Test
    public void lights() {
        byte[] bytes = ByteUtils.bytesFromHex("003601020100FF0000");

        com.highmobility.hmkit.Command.Incoming.IncomingCommand command = null;

        try {
            command = com.highmobility.hmkit.Command.Incoming.IncomingCommand.create(bytes);
        } catch (CommandParseException e) {
            fail("init failed");
        }

        assertTrue(command.getClass() == Lights.class);
        assertTrue(((Lights)command).getFrontExteriorLightState() == Constants.FrontExteriorLightState.ACTIVE_WITH_FULL_BEAM);
        assertTrue(((Lights)command).isRearExteriorLightActive() == true);
        assertTrue(((Lights)command).isInteriorLightActive() == false);
//        assertTrue(((Lights)command).getAmbientColor() == Color.RED); // Color is not mocked
    }
}
