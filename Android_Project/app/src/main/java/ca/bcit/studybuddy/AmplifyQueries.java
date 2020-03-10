package ca.bcit.studybuddy;

///**
// * PositionDataPoint models a point on a map.
// *
// * @author nickmcgrath
// */
//class PositionDataPoint {
//    public String name;
//    public String address;
//    public String type;
//    public double x;
//    public double y;
//
//    public PositionDataPoint(String name, String address, String type, double x, double y) {
//        this.name = name;
//        this.address = address;
//        this.type = type;
//        this.x = x;
//        this.y = y;
//
//    }
//}
//public class AmplifyQueries {
//    public void init(){
//        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
//            @Override
//            public void onResult(UserStateDetails userStateDetails) {
//                try {
//                    Amplify.addPlugin(new AWSApiPlugin());
//                    Amplify.configure(getApplicationContext());
//                    Log.i("ApiQuickstart", "All set and ready to go!");
//                } catch (Exception e) {
//                    Log.e("ApiQuickstart", e.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("ApiQuickstart", "Initialization error.", e);
//            }
//        });
//    }
//
//}
