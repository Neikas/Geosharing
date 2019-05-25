package com.example.erasmusapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.erasmusapp.R
import com.example.erasmusapp.Rest.ApiCon
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.eproject.protocol.MemberLocation
import android.support.v4.widget.SwipeRefreshLayout
import com.google.android.gms.maps.model.*
import org.eproject.protocol.UserProfile
import org.eproject.protocol.GroupState
import kotlin.concurrent.thread


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    var invKey: String? = null
    var userName: String? = null
    var picture: ByteArray? = null
    val apiCon = ApiCon();
    var ySwipeRefreshLayout: SwipeRefreshLayout? = null
    open var groupState: GroupState? = null
    open var userLocation: List<MemberLocation>? = null
    open var userProfile: List<UserProfile>? = null

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /*   ySwipeRefreshLayout =findViewById(R.id.swiperefresh)
        ySwipeRefreshLayout!!.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener { onMapReady(mMap) }
        )*/
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val googlePlex = CameraPosition.builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(13f)
                    .bearing(0f)
                    .tilt(45f)
                    .build()

                lastLocation = location
                invKey = intent.extras.getString("invKey")
                userName = intent.extras.getString("userName")
                picture = MainActivity.fotoToByteArray

                mMap.addMarker(
                    MarkerOptions().position(LatLng(location.latitude, location.longitude)).title("You").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
                )
                val joinGroup = apiCon.joinGroup(invKey!!, userName!!, picture!!, location.latitude, location.longitude)
                var membersLocation = joinGroup!!.locations
                var newMembers = joinGroup!!.newMembers



                /*val icon: BitmapDescriptor?= BitmapDescriptorFactory.fromResource(
            //                            BitmapFactory.decodeByteArray(array!!.get(i).picture, 0, array!!.get(i).picture.length)*/

                for(i in newMembers.indices){
                    var j = 1
                    var fotoByteArray = newMembers.get(i).picture

                    var bitMapFoto = BitmapFactory.decodeByteArray(fotoByteArray , 0 , fotoByteArray.size )

                    mMap.addMarker(
                        MarkerOptions().position(LatLng(membersLocation.get(j).longitude, membersLocation.get(j).latitude )).
                            title(newMembers.get(i).userName).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    )
                    j++
                }
            }
        }
    }
}


