package net.charapla.android.horoclock;

import java.util.HashMap;
import java.util.Calendar;

import android.util.Log;

public class PlanetCalc {
	private final static String TAG = "[PlanetCalc]";
	public static final int DEFAULT_PLACE = 28;

	// 北海道～沖縄の経度・緯度
	int PlaceLon[] = {0,8481,8445,8469,8453,8406,8422,8428,8429,8393,8344,8379,8408,8386,8379,8342,8233,8200,8173,8314,8291,8204,8303,8215,
					  8191,8152,8146,8131,8111,8150,8110,8054,7983,8036,7948,7889,8074,8043,7966,8012,7825,7818,7793,7845,7897,7886,7834,7661};
	int PlaceLat[] = {0,2584,2449,2382,2296,2383,2294,2265,2182,2194,2183,2151,2136,2140,2127,2274,2202,2194,2164,2140,2199,2123,2098,2111,
					  2084,2100,2101,2081,2081,2081,2053,2130,2128,2079,2064,2051,2044,2060,2030,2013,2016,1995,1964,1967,1994,1914,1896,1572};

	/* planet parameter */
	HashMap<String, Double> planet_param = new HashMap<String, Double>();
	//	lon_mon,ano_mon,lat_mon,elo_mon,alo_mon,lon_sun,ano_sun,lon_mer,ano_mer,lat_mer,
	//	lon_ven,ano_ven,lat_ven,lon_mar,ano_mar,lat_mar,lon_jup,ano_jup,lat_jup,
	//	lon_sat,ano_sat,lat_sat,lon_ura,ano_ura,lat_ura,lon_nep,ano_nep,lat_nep,
	//	lon_plu,ano_plu,lat_plu, p99;

	/* time parameter */
	double time_param = 0;

	/* planet position work */
	HashMap<String, Double> planet_pos = new HashMap<String, Double>();
	//	cl,cb,rv;

	private double floor_pi(double n) {return (n - Math.floor(n)) * 2 * Math.PI;}
	private double sinD(double x){return Math.sin( x * Math.PI / 180);}
	private double cosD(double x){return Math.cos( x * Math.PI / 180);}


	/****************************************************
	* planet parameter cal sub                          *
	*   input  : jd = julian day                        *
	*   output : lon_mon-lat_plu,p99 = planet parameter *
	*            time_prm = time parameter              *
	****************************************************/
	private void get_pr1(double jd) {
		double tt = jd - 2451545;
		time_param =  tt / 36525 + 1;

		planet_param.put("lon_mon", floor_pi(0.606434+0.03660110129*tt)); /* mean longitude of moon */
		planet_param.put("ano_mon", floor_pi(0.374897+0.03629164709*tt)); /* mean anomary of moon */
		planet_param.put("lat_mon", floor_pi(0.259091+0.03674819520*tt)); /* argument of latitude of moon */
		planet_param.put("elo_mon", floor_pi(0.827362+0.03386319198*tt)); /* mean elongation of moon from sun */
		planet_param.put("alo_mon", floor_pi(0.347343-0.00014709391*tt)); /* longitude of moon ascending node */
		planet_param.put("lon_sun", floor_pi(0.779072+0.00273790931*tt)); /* mean longitude of sun */
		planet_param.put("ano_sun", floor_pi(0.993126+0.00273777850*tt)); /* mean anomary of sun */
		planet_param.put("lon_mer", floor_pi(0.700695+0.01136771400*tt)); /* mean longitude of mercury */
		planet_param.put("ano_mer", floor_pi(0.485541+0.01136759566*tt)); /* mean anomary of mercury */
		planet_param.put("lat_mer", floor_pi(0.566441+0.01136762384*tt)); /* argument of latitude of mercury */
		planet_param.put("lon_ven", floor_pi(0.505498+0.00445046867*tt)); /* mean longitude of venus */
		planet_param.put("ano_ven", floor_pi(0.140023+0.00445036173*tt)); /* mean anomary of venus */
		planet_param.put("lat_ven", floor_pi(0.292498+0.00445040017*tt)); /* argument of latitude of venus */
		planet_param.put("lon_mar", floor_pi(0.987353+0.00145575328*tt)); /* mean longitude of mars */
		planet_param.put("ano_mar", floor_pi(0.053856+0.00145561327*tt)); /* mean anomary of mars */
		planet_param.put("lat_mar", floor_pi(0.849694+0.00145569465*tt)); /* argument of latitude of mars */
		planet_param.put("lon_jup", floor_pi(0.089608+0.00023080893*tt)); /* mean longitude of jupiter */
		planet_param.put("ano_jup", floor_pi(0.056531+0.00023080893*tt)); /* mean anomary of jupiter */
		planet_param.put("lat_jup", floor_pi(0.814794+0.00023080893*tt)); /* argument of latitude of jupiter */
		planet_param.put("lon_sat", floor_pi(0.133295+0.00009294371*tt)); /* mean longitude of saturn */
		planet_param.put("ano_sat", floor_pi(0.882987+0.00009294371*tt)); /* mean anomary of saturn */
		planet_param.put("lat_sat", floor_pi(0.821218+0.00009294371*tt)); /* argument of latitude of saturn */
		planet_param.put("lon_ura", floor_pi(0.870169+0.00003269438*tt)); /* mean longitude of uranus */
		planet_param.put("ano_ura", floor_pi(0.400589+0.00003269438*tt)); /* mean anomary of uranus */
		planet_param.put("lat_ura", floor_pi(0.664614+0.00003265562*tt)); /* argument of latitude of uranus */
		planet_param.put("lon_nep", floor_pi(0.846912+0.00001672092*tt)); /* mean longitude of neptune */
		planet_param.put("ano_nep", floor_pi(0.725368+0.00001672092*tt)); /* mean anomary of neptune */
		planet_param.put("lat_nep", floor_pi(0.480856+0.00001663715*tt)); /* argument of latitude of neptune */
		planet_param.put("lon_plu", floor_pi(0.663854+0.00001115482*tt)); /* mean longitude of pluto */
		planet_param.put("ano_plu", floor_pi(0.041020+0.00001104864*tt)); /* mean anomary of pluto */
		planet_param.put("lat_plu", floor_pi(0.357355+0.00001104864*tt)); /* argument of latitude of pluto */

		planet_param.put("p99", floor_pi((1934*time_param-1789)/360));
	}

	/*************************************
	* sun position caliculate sub 1      *
	*   (geocentric ecliptic)            *
	*  output : cl = longitude [sec,deg] *
	*           cb = latitude  [sec,deg] *
	*           rv = radius    [AU]      *
	*************************************/
	private void sun_cal1() {
		double cl = 1296000*planet_param.get("lon_sun")/Math.PI/2;
		cl += 6910*Math.sin(planet_param.get("ano_sun"));
		cl +=   72*Math.sin(2*planet_param.get("ano_sun"));
		cl -=   17*time_param*Math.sin(planet_param.get("ano_sun"));
		cl -=    7*Math.cos(planet_param.get("ano_sun")-planet_param.get("ano_jup"));
		cl +=    6*Math.sin(planet_param.get("lon_mon")-planet_param.get("lon_sun"));
		cl +=    5*Math.sin(4*planet_param.get("ano_sun")-8*planet_param.get("ano_mar")+3*planet_param.get("ano_jup"));
		cl -=    5*Math.cos(2*planet_param.get("ano_sun")-2*planet_param.get("ano_ven"));
		cl -=    4*Math.sin(planet_param.get("ano_sun")-planet_param.get("ano_ven"));
		cl +=    4*Math.cos(4*planet_param.get("ano_sun")-8*planet_param.get("ano_mar")+3*planet_param.get("ano_jup"));
		cl +=    3*Math.sin(2*planet_param.get("ano_sun")-2*planet_param.get("ano_ven"));
		cl -=    3*Math.sin(planet_param.get("ano_jup"));
		cl -=    3*Math.sin(2*planet_param.get("ano_sun")-2*planet_param.get("ano_jup"));
		planet_pos.put("cl", cl);

		planet_pos.put("cb", (double)0);

		double rv = 1.00014;
		rv -= 0.01675*Math.cos(planet_param.get("ano_sun"));
		rv -= 0.00014*Math.cos(2*planet_param.get("ano_sun"));
		planet_pos.put("rv", rv);
	}

	/**************************
	* moon position cal sub 1 *
	*   (geocentric ecliptic) *
	**************************/
	private void moon_cal1() {
		double cl = 1296000*planet_param.get("lon_mon")/Math.PI/2;
		cl +=  22640*Math.sin(planet_param.get("ano_mon"));
		cl -=   4586*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("elo_mon"));
		cl +=   2370*Math.sin(2*planet_param.get("elo_mon"));
		cl +=    769*Math.sin(2*planet_param.get("ano_mon"));
		cl -=    668*Math.sin(planet_param.get("ano_sun"));
		cl -=    412*Math.sin(2*planet_param.get("lat_mon"));
		cl -=    212*Math.sin(2*planet_param.get("ano_mon")-2*planet_param.get("elo_mon"));
		cl -=    206*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=    192*Math.sin(planet_param.get("ano_mon")+2*planet_param.get("elo_mon"));
		cl +=    165*Math.sin(2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cl +=    148*Math.sin(planet_param.get("ano_mon")-planet_param.get("ano_sun"));
		cl -=    125*Math.sin(planet_param.get("elo_mon"));
		cl -=    110*Math.sin(planet_param.get("ano_mon")+planet_param.get("ano_sun"));
		cl -=     55*Math.sin(2*planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cl -=     45*Math.sin(planet_param.get("ano_mon")+2*planet_param.get("lat_mon"));
		cl +=     40*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("lat_mon"));
		cl -=     38*Math.sin(planet_param.get("ano_mon")-4*planet_param.get("elo_mon"));
		cl +=     36*Math.sin(3*planet_param.get("ano_mon"));
		cl -=     31*Math.sin(2*planet_param.get("ano_mon")-4*planet_param.get("elo_mon"));
		cl +=     28*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cl -=     24*Math.sin(2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=     19*Math.sin(planet_param.get("ano_mon")-planet_param.get("elo_mon"));
		cl +=     18*Math.sin(planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=     15*Math.sin(planet_param.get("ano_mon")+2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cl +=     14*Math.sin(2*planet_param.get("ano_mon")+2*planet_param.get("elo_mon"));
		cl +=     14*Math.sin(4*planet_param.get("elo_mon"));
		cl -=     13*Math.sin(3*planet_param.get("ano_mon")-2*planet_param.get("elo_mon"));
		cl -=     11*Math.sin(planet_param.get("ano_mon")+16*planet_param.get("lon_sun")-18*planet_param.get("lon_ven"));
		cl +=     10*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("ano_sun"));
		cl +=      9*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cl +=      9*Math.cos(planet_param.get("ano_mon")+16*planet_param.get("lon_sun")-18*planet_param.get("lon_ven"));
		cl -=      9*Math.sin(2*planet_param.get("ano_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl -=      8*Math.sin(planet_param.get("ano_mon")+planet_param.get("elo_mon"));
		cl +=      8*Math.sin(2*planet_param.get("elo_mon")-2*planet_param.get("ano_sun"));
		cl -=      8*Math.sin(2*planet_param.get("ano_mon")+planet_param.get("ano_sun"));
		cl -=      7*Math.sin(2*planet_param.get("ano_sun"));
		cl -=      7*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("elo_mon")+2*planet_param.get("ano_sun"));
		cl +=      7*Math.sin(planet_param.get("alo_mon"));
		cl -=      6*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cl -=      6*Math.sin(2*planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cl -=      4*Math.sin(planet_param.get("ano_mon")-4*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=      4*time_param*Math.cos(planet_param.get("ano_mon")+16*planet_param.get("lon_sun")-18*planet_param.get("lon_ven"));
		cl -=      4*Math.sin(2*planet_param.get("ano_mon")+2*planet_param.get("lat_mon"));
		cl +=      4*time_param*Math.sin(planet_param.get("ano_mon")+16*planet_param.get("lon_sun")-18*planet_param.get("lon_ven"));
		cl +=      3*Math.sin(planet_param.get("ano_mon")-3*planet_param.get("elo_mon"));
		cl -=      3*Math.sin(planet_param.get("ano_mon")+2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl -=      3*Math.sin(2*planet_param.get("ano_mon")-4*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=      3*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("ano_sun"));
		cl +=      3*Math.sin(planet_param.get("ano_mon")-2*planet_param.get("elo_mon")-2*planet_param.get("ano_sun"));
		cl -=      2*Math.sin(2*planet_param.get("ano_mon")-2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cl -=      2*Math.sin(2*planet_param.get("lat_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cl +=      2*Math.sin(planet_param.get("ano_mon")+4*planet_param.get("elo_mon"));
		cl +=      2*Math.sin(4*planet_param.get("ano_mon"));
		cl +=      2*Math.sin(4*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cl +=      2*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("elo_mon"));
		planet_pos.put("cl", cl);

		double cb = 18461*Math.sin(planet_param.get("lat_mon"));
		cb += 1010*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon"));
		cb += 1000*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon"));
		cb -=  624*Math.sin(planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb -=  199*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb -=  167*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb +=  117*Math.sin(planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cb +=   62*Math.sin(2*planet_param.get("ano_mon")+planet_param.get("lat_mon"));
		cb +=   33*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cb +=   32*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("lat_mon"));
		cb -=   30*Math.sin(planet_param.get("lat_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cb -=   16*Math.sin(2*planet_param.get("ano_mon")+planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb +=   15*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cb +=   12*Math.sin(planet_param.get("lat_mon")-2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cb -=    9*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cb -=    8*Math.sin(planet_param.get("lat_mon")+planet_param.get("alo_mon"));
		cb +=    8*Math.sin(planet_param.get("lat_mon")+2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cb -=    7*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")-2*planet_param.get("elo_mon")+planet_param.get("ano_sun"));
		cb +=    7*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")-planet_param.get("ano_sun"));
		cb -=    7*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")-4*planet_param.get("elo_mon"));
		cb -=    6*Math.sin(planet_param.get("lat_mon")+planet_param.get("ano_sun"));
		cb -=    6*Math.sin(3*planet_param.get("lat_mon"));
		cb +=    6*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")-planet_param.get("ano_sun"));
		cb -=    5*Math.sin(planet_param.get("lat_mon")+planet_param.get("elo_mon"));
		cb -=    5*Math.sin(planet_param.get("ano_mon")+planet_param.get("lat_mon")+planet_param.get("ano_sun"));
		cb -=    5*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")+planet_param.get("ano_sun"));
		cb +=    5*Math.sin(planet_param.get("lat_mon")-planet_param.get("ano_sun"));
		cb +=    5*Math.sin(planet_param.get("lat_mon")-planet_param.get("elo_mon"));
		cb +=    4*Math.sin(3*planet_param.get("ano_mon")+planet_param.get("lat_mon"));
		cb -=    4*Math.sin(planet_param.get("lat_mon")-4*planet_param.get("elo_mon"));
		cb -=    3*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")-4*planet_param.get("elo_mon"));
		cb +=    3*Math.sin(planet_param.get("ano_mon")-3*planet_param.get("lat_mon"));
		cb -=    2*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("lat_mon")-4*planet_param.get("elo_mon"));
		cb -=    2*Math.sin(3*planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb +=    2*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("lat_mon")+2*planet_param.get("elo_mon"));
		cb +=    2*Math.sin(planet_param.get("ano_mon")-planet_param.get("lat_mon")+2*planet_param.get("elo_mon")-planet_param.get("ano_sun"));
		cb +=    2*Math.sin(2*planet_param.get("ano_mon")-planet_param.get("lat_mon")-2*planet_param.get("elo_mon"));
		cb +=    2*Math.sin(3*planet_param.get("ano_mon")-planet_param.get("lat_mon"));
		planet_pos.put("cb", cb);
	}

	/*****************************
	* mercury position cal sub 1 *
	*   (heriocentric ecliptic)  *
	*****************************/
	private void mercury_cal1() {
		double cl  = 1296000*planet_param.get("lon_mer")/Math.PI/2;
		cl +=  84378*Math.sin(planet_param.get("ano_mer"));
		cl +=  10733*Math.sin(2*planet_param.get("ano_mer"));
		cl +=   1892*Math.sin(3*planet_param.get("ano_mer"));
		cl -=    646*Math.sin(2*planet_param.get("lat_mer"));
		cl +=    381*Math.sin(4*planet_param.get("ano_mer"));
		cl -=    306*Math.sin(planet_param.get("ano_mer")-2*planet_param.get("lat_mer"));
		cl -=    274*Math.sin(planet_param.get("ano_mer")+2*planet_param.get("lat_mer"));
		cl -=     92*Math.sin(2*planet_param.get("ano_mer")+2*planet_param.get("lat_mer"));
		cl +=     83*Math.sin(5*planet_param.get("ano_mer"));
		cl -=     28*Math.sin(3*planet_param.get("ano_mer")+2*planet_param.get("lat_mer"));
		cl +=     25*Math.sin(2*planet_param.get("ano_mer")-2*planet_param.get("lat_mer"));
		cl +=     19*Math.sin(6*planet_param.get("ano_mer"));
		cl -=      9*Math.sin(4*planet_param.get("ano_mer")+2*planet_param.get("lat_mer"));
		cl +=      8*time_param*Math.sin(planet_param.get("ano_mer"));
		cl +=      7*Math.cos(2*planet_param.get("ano_mer")-5*planet_param.get("ano_ven"));
		planet_pos.put("cl", cl);

		double cb  =24134*Math.sin(planet_param.get("lat_mer"));
		cb += 5180*Math.sin(planet_param.get("ano_mer")-planet_param.get("lat_mer"));
		cb += 4910*Math.sin(planet_param.get("ano_mer")+planet_param.get("lat_mer"));
		cb += 1124*Math.sin(2*planet_param.get("ano_mer")+planet_param.get("lat_mer"));
		cb +=  271*Math.sin(3*planet_param.get("ano_mer")+planet_param.get("lat_mer"));
		cb +=  132*Math.sin(2*planet_param.get("ano_mer")-planet_param.get("lat_mer"));
		cb +=   67*Math.sin(4*planet_param.get("ano_mer")+planet_param.get("lat_mer"));
		cb +=   18*Math.sin(3*planet_param.get("ano_mer")-planet_param.get("lat_mer"));
		cb +=   17*Math.sin(5*planet_param.get("ano_mer")+planet_param.get("lat_mer"));
		cb -=   10*Math.sin(3*planet_param.get("lat_mer"));
		cb -=    9*Math.sin(planet_param.get("ano_mer")-3*planet_param.get("lat_mer"));
		planet_pos.put("cb", cb);

		double rv  =0.39528;
		rv -=0.07834*Math.cos(planet_param.get("ano_mer"));
		rv -=0.00795*Math.cos(2*planet_param.get("ano_mer"));
		rv -=0.00121*Math.cos(3*planet_param.get("ano_mer"));
		rv -=0.00022*Math.cos(4*planet_param.get("ano_mer"));
		planet_pos.put("rv", rv);
	}

	/***************************
	* venus position cal sub 1 *
	***************************/
	private void venus_cal1() {
		double cl =1296000*planet_param.get("lon_ven")/Math.PI/2;
		cl +=   2814*Math.sin(planet_param.get("ano_ven"));
		cl -=    181*Math.sin(2*planet_param.get("lat_ven"));
		cl -=     20*time_param*Math.sin(planet_param.get("ano_ven"));
		cl +=     12*Math.sin(2*planet_param.get("ano_ven"));
		cl -=     10*Math.cos(2*planet_param.get("ano_sun")-2*planet_param.get("ano_ven"));
		cl +=      7*Math.cos(3*planet_param.get("ano_sun")-3*planet_param.get("ano_ven"));
		planet_pos.put("cl", cl);

		double cb =12215*Math.sin(planet_param.get("lat_ven"));
		cb +=   83*Math.sin(planet_param.get("ano_ven")+planet_param.get("lat_ven"));
		cb +=   83*Math.sin(planet_param.get("ano_ven")-planet_param.get("lat_ven"));
		planet_pos.put("cb", cb);

		double rv =0.72335;
		rv -=0.00493*Math.cos(planet_param.get("ano_ven"));
		planet_pos.put("rv", rv);
	}

	/**************************
	* mars position cal sub 1 *
	**************************/
	private void mars_cal1() {
		double cl =1296000*planet_param.get("lon_mar")/Math.PI/2;
		cl +=  38451*Math.sin(planet_param.get("ano_mar"));
		cl +=   2238*Math.sin(2*planet_param.get("ano_mar"));
		cl +=    181*Math.sin(3*planet_param.get("ano_mar"));
		cl -=     52*Math.sin(2*planet_param.get("lat_mar"));
		cl +=     37*time_param*Math.sin(planet_param.get("ano_mar"));
		cl -=     22*Math.cos(planet_param.get("ano_mar")-2*planet_param.get("ano_jup"));
		cl -=     19*Math.sin(planet_param.get("ano_mar")-planet_param.get("ano_jup"));
		cl +=     17*Math.cos(planet_param.get("ano_mar")-planet_param.get("ano_jup"));
		cl +=     17*Math.sin(4*planet_param.get("ano_mar"));
		cl -=     16*Math.cos(2*planet_param.get("ano_mar")-2*planet_param.get("ano_jup"));
		cl +=     13*Math.cos(planet_param.get("ano_sun")-2*planet_param.get("ano_mar"));
		cl -=     10*Math.sin(planet_param.get("ano_mar")-2*planet_param.get("lat_mar"));
		cl -=     10*Math.sin(planet_param.get("ano_mar")+2*planet_param.get("lat_mar"));
		cl +=      7*Math.cos(planet_param.get("ano_sun")-planet_param.get("ano_mar"));
		cl -=      7*Math.cos(2*planet_param.get("ano_sun")-3*planet_param.get("ano_mar"));
		cl -=      5*Math.sin(planet_param.get("ano_ven")-3*planet_param.get("ano_mar"));
		cl -=      5*Math.sin(planet_param.get("ano_sun")-planet_param.get("ano_mar"));
		cl -=      5*Math.sin(planet_param.get("ano_sun")-2*planet_param.get("ano_mar"));
		cl -=      4*Math.cos(2*planet_param.get("ano_sun")-4*planet_param.get("ano_mar"));
		cl +=      4*time_param*Math.sin(2*planet_param.get("ano_mar"));
		cl +=      4*Math.cos(planet_param.get("ano_jup"));
		cl +=      3*Math.cos(planet_param.get("ano_ven")-3*planet_param.get("ano_mar"));
		cl +=      3*Math.sin(2*planet_param.get("ano_mar")-2*planet_param.get("ano_jup"));
		planet_pos.put("cl", cl);

		double cb =6603*Math.sin(planet_param.get("lat_mar"));
		cb += 622*Math.sin(planet_param.get("ano_mar")-planet_param.get("lat_mar"));
		cb += 615*Math.sin(planet_param.get("ano_mar")+planet_param.get("lat_mar"));
		cb +=  64*Math.sin(2*planet_param.get("ano_mar")+planet_param.get("lat_mar"));
		planet_pos.put("cb", cb);

		double rv =1.53031;
		rv -=0.14170*Math.cos(planet_param.get("ano_mar"));
		rv -=0.00660*Math.cos(2*planet_param.get("ano_mar"));
		rv -=0.00047*Math.cos(3*planet_param.get("ano_mar"));
		planet_pos.put("rv", rv);
	}

	/*****************************
	* jupiter position cal sub 1 *
	*****************************/
	private void jupiter_cal1() {
		double cl =1296000*planet_param.get("lon_jup")/Math.PI/2;
		cl +=  19934*Math.sin(planet_param.get("ano_jup"));
		cl +=   5023*time_param;
		cl +=   2511;
		cl +=   1093*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=    601*Math.sin(2*planet_param.get("ano_jup"));
		cl -=    479*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=    185*Math.sin(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=    137*Math.sin(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=    131*Math.sin(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=     79*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl -=     76*Math.cos(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl -=     74*time_param*Math.cos(planet_param.get("ano_jup"));
		cl +=     68*time_param*Math.sin(planet_param.get("ano_jup"));
		cl +=     66*Math.cos(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=     63*Math.cos(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=     53*Math.cos(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=     49*Math.sin(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl -=     43*time_param*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=     37*Math.cos(planet_param.get("ano_jup"));
		cl +=     25*Math.sin(2*planet_param.get("lon_jup"));
		cl +=     25*Math.sin(3*planet_param.get("ano_jup"));
		cl -=     23*Math.sin(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=     19*time_param*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=     17*Math.cos(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=     17*Math.cos(3*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl -=     14*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl -=     13*Math.sin(3*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl -=      9*Math.cos(2*planet_param.get("lon_jup"));
		cl +=      9*Math.cos(planet_param.get("ano_sat"));
		cl -=      9*Math.sin(planet_param.get("ano_sat"));
		cl -=      9*Math.sin(3*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=      9*Math.sin(4*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=      9*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+3*planet_param.get("ano_ura"));
		cl -=      8*Math.cos(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl +=      7*Math.cos(3*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl -=      7*Math.cos(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl -=      7*Math.sin(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl -=      7*Math.sin(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=      6*Math.cos(4*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=      6*Math.sin(3*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=      5*Math.cos(2*planet_param.get("ano_sat"));
		cl -=      4*Math.sin(4*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl -=      4*Math.cos(3*planet_param.get("ano_sat"));
		cl +=      4*Math.cos(2*planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl -=      4*Math.cos(3*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl -=      4*time_param*Math.cos(2*planet_param.get("ano_jup"));
		cl +=      3*time_param*Math.sin(2*planet_param.get("ano_jup"));
		cl +=      3*Math.cos(5*planet_param.get("ano_sat"));
		cl +=      3*Math.cos(5*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl +=      3*Math.sin(2*planet_param.get("ano_sat"));
		cl -=      2*Math.sin(2*planet_param.get("lon_jup")-planet_param.get("ano_jup"));
		cl +=      2*Math.sin(2*planet_param.get("lon_jup")+planet_param.get("ano_jup"));
		cl -=      2*time_param*Math.sin(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=      2*time_param*Math.sin(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		planet_pos.put("cl", cl);

		double cb =-4692*Math.cos(planet_param.get("ano_jup"));
		cb +=  259*Math.sin(planet_param.get("ano_jup"));
		cb +=  227;
		cb -=  227*Math.cos(2*planet_param.get("ano_jup"));
		cb +=   30*time_param*Math.sin(planet_param.get("ano_jup"));
		cb +=   21*time_param*Math.cos(planet_param.get("ano_jup"));
		cb +=   16*Math.sin(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cb -=   13*Math.sin(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cb -=   12*Math.cos(3*planet_param.get("ano_jup"));
		cb +=   12*Math.sin(2*planet_param.get("ano_jup"));
		cb +=    7*Math.cos(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cb -=    5*Math.cos(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		planet_pos.put("cb", cb);

		double rv =5.20883;
		rv -=0.25122*Math.cos(planet_param.get("ano_jup"));
		rv -=0.00604*Math.cos(2*planet_param.get("ano_jup"));
		rv +=0.00260*Math.cos(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv -=0.00170*Math.cos(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv -=0.00106*Math.sin(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv -=0.00091*time_param*Math.sin(planet_param.get("ano_jup"));
		rv -=0.00084*time_param*Math.cos(planet_param.get("ano_jup"));
		rv +=0.00069*Math.sin(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		rv -=0.00067*Math.sin(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv +=0.00066*Math.sin(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv +=0.00063*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		rv -=0.00051*Math.cos(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		rv -=0.00046*Math.sin(planet_param.get("ano_jup"));
		rv -=0.00029*Math.cos(planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv +=0.00027*Math.cos(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv -=0.00022*Math.cos(3*planet_param.get("ano_jup"));
		rv -=0.00021*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		planet_pos.put("rv", rv);
	}

	/****************************
	* saturn position cal sub 1 *
	****************************/
	private void saturn_cal1() {
		double cl =1296000*planet_param.get("lon_sat")/Math.PI/2;
		cl +=  23045*Math.sin(planet_param.get("ano_sat"));
		cl +=   5014*time_param;
		cl -=   2689*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=   2507;
		cl +=   1177*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=    826*Math.cos(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=    802*Math.sin(2*planet_param.get("ano_sat"));
		cl +=    425*Math.sin(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl -=    229*time_param*Math.cos(planet_param.get("ano_sat"));
		cl -=    153*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cl -=    142*time_param*Math.sin(planet_param.get("ano_sat"));
		cl -=    114*Math.cos(planet_param.get("ano_sat"));
		cl +=    101*time_param*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl -=     70*Math.cos(2*planet_param.get("lon_sat"));
		cl +=     67*Math.sin(2*planet_param.get("lon_sat"));
		cl +=     66*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cl +=     60*time_param*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=     41*Math.sin(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=     39*Math.sin(3*planet_param.get("ano_sat"));
		cl +=     31*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl +=     31*Math.sin(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl -=     29*Math.cos(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl -=     28*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+3*planet_param.get("ano_ura"));
		cl +=     28*Math.cos(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=     22*time_param*Math.sin(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl -=     22*Math.sin(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=     20*Math.sin(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=     20*Math.cos(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl +=     19*Math.cos(2*planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=     19*Math.sin(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl -=     17*time_param*Math.cos(2*planet_param.get("ano_sat"));
		cl -=     16*Math.cos(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl -=     12*Math.sin(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=     12*Math.cos(planet_param.get("ano_jup"));
		cl -=     12*Math.sin(2*planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl -=     11*time_param*Math.sin(2*planet_param.get("ano_sat"));
		cl -=     11*Math.cos(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cl +=     10*Math.sin(2*planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=     10*Math.cos(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=      9*Math.sin(4*planet_param.get("ano_jup")-9*planet_param.get("ano_sat"));
		cl -=      8*Math.sin(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl -=      8*Math.cos(2*planet_param.get("lon_sat")+planet_param.get("ano_sat"));
		cl +=      8*Math.cos(2*planet_param.get("lon_sat")-planet_param.get("ano_sat"));
		cl +=      8*Math.cos(planet_param.get("ano_sat")-planet_param.get("ano_ura"));
		cl -=      8*Math.sin(2*planet_param.get("lon_sat")-planet_param.get("ano_sat"));
		cl +=      7*Math.sin(2*planet_param.get("lon_sat")+planet_param.get("ano_sat"));
		cl -=      7*Math.cos(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl -=      7*Math.cos(2*planet_param.get("ano_sat"));
		cl -=      6*time_param*Math.sin(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl +=      6*time_param*Math.cos(4*planet_param.get("ano_jup")-10*planet_param.get("ano_sat"));
		cl +=      6*time_param*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cl -=      5*Math.sin(3*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cl -=      5*Math.cos(3*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl -=      5*Math.cos(2*planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl +=      5*Math.sin(3*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=      5*Math.sin(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cl +=      4*Math.sin(3*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cl +=      4*Math.sin(3*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cl +=      4*time_param*Math.cos(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=      3*time_param*Math.cos(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=      3*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+3*planet_param.get("ano_ura"));
		cl -=      3*time_param*Math.sin(2*planet_param.get("lon_sat"));
		cl +=      3*time_param*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cl -=      3*time_param*Math.cos(2*planet_param.get("lon_sat"));
		cl +=      3*Math.cos(3*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cl +=      3*Math.cos(4*planet_param.get("ano_jup")-9*planet_param.get("ano_sat"));
		cl +=      3*Math.sin(3*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cl +=      3*Math.sin(2*planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl +=      3*Math.sin(planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=      2*Math.cos(3*planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=      2*time_param*Math.sin(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cl +=      2*Math.sin(4*planet_param.get("ano_sat"));
		cl -=      2*Math.cos(3*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl -=      2*Math.cos(2*planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cl -=      2*Math.sin(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat")+3*planet_param.get("ano_ura"));
		cl +=      2*Math.cos(planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cl +=      2*Math.cos(4*planet_param.get("ano_jup")-11*planet_param.get("ano_sat"));
		cl -=      2*Math.sin(planet_param.get("ano_sat")-planet_param.get("ano_ura"));
		planet_pos.put("cl", cl);

		double cb =8297*Math.sin(planet_param.get("ano_sat"));
		cb -=3346*Math.cos(planet_param.get("ano_sat"));
		cb += 462*Math.sin(2*planet_param.get("ano_sat"));
		cb -= 189*Math.cos(2*planet_param.get("ano_sat"));
		cb += 185;
		cb +=  79*time_param*Math.cos(planet_param.get("ano_sat"));
		cb -=  71*Math.cos(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cb +=  46*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cb -=  45*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		cb +=  29*Math.sin(3*planet_param.get("ano_sat"));
		cb -=  20*Math.cos(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cb +=  18*time_param*Math.sin(planet_param.get("ano_sat"));
		cb -=  14*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cb -=  11*Math.cos(3*planet_param.get("ano_sat"));
		cb -=  10*time_param;
		cb +=   9*Math.sin(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cb +=   8*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cb -=   6*Math.sin(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cb +=   5*Math.sin(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cb -=   5*Math.cos(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		cb +=   4*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		cb -=   4*time_param*Math.sin(2*planet_param.get("ano_sat"));
		cb -=   3*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		cb +=   3*Math.cos(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		cb +=   3*time_param*Math.sin(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		cb +=   3*Math.sin(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		cb +=   2*Math.sin(4*planet_param.get("ano_sat"));
		cb -=   2*Math.cos(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		planet_pos.put("cb", cb);

		double rv =9.55774;
		rv -=0.53252*Math.cos(planet_param.get("ano_sat"));
		rv -=0.01878*Math.sin(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		rv -=0.01482*Math.cos(2*planet_param.get("ano_sat"));
		rv +=0.00817*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		rv -=0.00539*Math.cos(planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv -=0.00524*time_param*Math.sin(planet_param.get("ano_sat"));
		rv +=0.00349*Math.sin(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv +=0.00347*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		rv +=0.00328*time_param*Math.cos(planet_param.get("ano_sat"));
		rv -=0.00225*Math.sin(planet_param.get("ano_sat"));
		rv +=0.00149*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat"));
		rv -=0.00126*Math.cos(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv +=0.00104*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_sat"));
		rv +=0.00101*Math.cos(2*planet_param.get("ano_jup")-5*planet_param.get("ano_sat"));
		rv +=0.00098*Math.cos(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		rv -=0.00073*Math.cos(2*planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		rv -=0.00062*Math.cos(3*planet_param.get("ano_sat"));
		rv +=0.00042*Math.sin(2*planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		rv +=0.00041*Math.sin(2*planet_param.get("ano_jup")-2*planet_param.get("ano_sat"));
		rv -=0.00040*Math.sin(planet_param.get("ano_jup")-3*planet_param.get("ano_sat"));
		rv +=0.00040*Math.cos(2*planet_param.get("ano_jup")-4*planet_param.get("ano_sat"));
		rv -=0.00028*time_param;
		rv -=0.00023*Math.sin(planet_param.get("ano_jup"));
		rv +=0.00020*Math.sin(2*planet_param.get("ano_jup")-7*planet_param.get("ano_sat"));
		planet_pos.put("rv", rv);
	}

	/****************************
	* uranus position cal sub 1 *
	****************************/
	private void uranus_cal1() {
		double cl  =1296000*planet_param.get("lon_ura")/Math.PI/2;
		cl +=  19397*Math.sin(planet_param.get("ano_ura"));
		cl +=    570*Math.sin(2*planet_param.get("ano_ura"));
		cl -=    536*time_param*Math.cos(planet_param.get("ano_ura"));
		cl +=    143*Math.sin(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl +=    110*time_param*Math.sin(planet_param.get("ano_ura"));
		cl +=    102*Math.sin(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=     76*Math.cos(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl -=     49*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_ura"));
		cl +=     32*time_param*time_param;
		cl -=     30*time_param*Math.cos(2*planet_param.get("ano_ura"));
		cl +=     29*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+3*planet_param.get("ano_ura"));
		cl +=     29*Math.cos(2*planet_param.get("ano_ura")-2*planet_param.get("ano_nep"));
		cl -=     28*Math.cos(planet_param.get("ano_ura")-planet_param.get("ano_nep"));
		cl +=     23*Math.sin(3*planet_param.get("ano_ura"));
		cl -=     21*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_ura"));
		cl +=     20*Math.sin(planet_param.get("ano_ura")-planet_param.get("ano_nep"));
		cl +=     20*Math.cos(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl -=     19*Math.cos(planet_param.get("ano_sat")-planet_param.get("ano_ura"));
		cl +=     17*Math.sin(2*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl +=     14*Math.sin(3*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl +=     13*Math.sin(planet_param.get("ano_sat")-planet_param.get("ano_ura"));
		cl -=     12*time_param*time_param*Math.cos(planet_param.get("ano_ura"));
		cl -=     12*Math.cos(planet_param.get("ano_ura"));
		cl +=     10*Math.sin(2*planet_param.get("ano_ura")-2*planet_param.get("ano_nep"));
		cl -=      9*Math.sin(2*planet_param.get("lat_ura"));
		cl -=      9*time_param*time_param*Math.sin(planet_param.get("ano_ura"));
		cl +=      9*Math.cos(2*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl +=      8*time_param*Math.cos(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		cl +=      7*time_param*Math.cos(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl -=      7*time_param*Math.sin(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		cl +=      7*time_param*Math.sin(2*planet_param.get("ano_ura"));
		cl +=      6*Math.sin(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+2*planet_param.get("ano_ura"));
		cl +=      6*Math.cos(2*planet_param.get("ano_jup")-6*planet_param.get("ano_sat")+2*planet_param.get("ano_ura"));
		cl +=      5*Math.sin(planet_param.get("ano_sat")-4*planet_param.get("ano_ura"));
		cl -=      4*Math.sin(3*planet_param.get("ano_ura")-4*planet_param.get("ano_nep"));
		cl +=      4*Math.cos(3*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl -=      3*Math.cos(planet_param.get("ano_nep"));
		cl -=      2*Math.sin(planet_param.get("ano_nep"));
		planet_pos.put("cl", cl);

		double cb =2775*Math.sin(planet_param.get("lat_ura"));
		cb += 131*Math.sin(planet_param.get("ano_ura")-planet_param.get("lat_ura"));
		cb += 130*Math.sin(planet_param.get("ano_ura")+planet_param.get("lat_ura"));
		planet_pos.put("cb", cb);

		double rv =19.21216;
		rv -= 0.90154*Math.cos(planet_param.get("ano_ura"));
		rv -= 0.02488*time_param*Math.sin(planet_param.get("ano_ura"));
		rv -= 0.02121*Math.cos(2*planet_param.get("ano_ura"));
		rv -= 0.00585*Math.cos(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		rv -= 0.00508*time_param*Math.cos(planet_param.get("ano_ura"));
		rv -= 0.00451*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_ura"));
		rv += 0.00336*Math.sin(planet_param.get("ano_sat")-planet_param.get("ano_ura"));
		rv += 0.00198*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_ura"));
		rv += 0.00118*Math.cos(planet_param.get("ano_sat")-3*planet_param.get("ano_ura"));
		rv += 0.00107*Math.sin(planet_param.get("ano_sat")-2*planet_param.get("ano_ura"));
		rv -= 0.00103*time_param*Math.sin(2*planet_param.get("ano_ura"));
		rv -= 0.00081*Math.cos(3*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		planet_pos.put("rv", rv);
	}

	/*****************************
	* neptune position cal sub 1 *
	*****************************/
	private void neptune_cal1() {
		double cl =1296000*planet_param.get("lon_nep")/Math.PI/2;
		cl +=   3523*Math.sin(planet_param.get("ano_nep"));
		cl -=     50*Math.sin(2*planet_param.get("lat_nep"));
		cl -=     43*time_param*Math.cos(planet_param.get("ano_nep"));
		cl +=     29*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_nep"));
		cl +=     19*Math.sin(2*planet_param.get("ano_nep"));
		cl -=     18*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_nep"));
		cl +=     13*Math.cos(planet_param.get("ano_sat")-planet_param.get("ano_nep"));
		cl +=     13*Math.sin(planet_param.get("ano_sat")-planet_param.get("ano_nep"));
		cl -=      9*Math.sin(2*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl +=      9*Math.cos(2*planet_param.get("ano_ura")-2*planet_param.get("ano_nep"));
		cl -=      5*Math.cos(2*planet_param.get("ano_ura")-3*planet_param.get("ano_nep"));
		cl -=      4*time_param*Math.sin(planet_param.get("ano_nep"));
		cl +=      4*Math.cos(planet_param.get("ano_ura")-2*planet_param.get("ano_nep"));
		cl +=      4*time_param*time_param*Math.sin(planet_param.get("ano_nep"));
		planet_pos.put("cl", cl);

		double cb =6404*Math.sin(planet_param.get("lat_nep"));
		cb +=  55*Math.sin(planet_param.get("ano_nep")+planet_param.get("lat_nep"));
		cb +=  55*Math.sin(planet_param.get("ano_nep")-planet_param.get("lat_nep"));
		cb -=  33*time_param*Math.sin(planet_param.get("lat_nep"));
		planet_pos.put("cb", cb);

		double rv =30.07175;
		rv -= 0.25701*Math.cos(planet_param.get("ano_nep"));
		rv -= 0.00787*Math.cos(2*planet_param.get("lon_ura")-planet_param.get("ano_ura")-2*planet_param.get("lon_nep"));
		rv += 0.00409*Math.cos(planet_param.get("ano_jup")-planet_param.get("ano_nep"));
		rv -= 0.00314*time_param*Math.sin(planet_param.get("ano_nep"));
		rv += 0.00250*Math.sin(planet_param.get("ano_jup")-planet_param.get("ano_nep"));
		rv -= 0.00194*Math.sin(planet_param.get("ano_sat")-planet_param.get("ano_nep"));
		rv += 0.00185*Math.cos(planet_param.get("ano_sat")-planet_param.get("ano_nep"));
		planet_pos.put("rv", rv);
	}

	/***************************
	* Pluto position cal sub 1 *
	***************************/
	private void pluto_cal1() {
		double cl =1296000*planet_param.get("lon_plu")/Math.PI/2;
		cl += 101577*Math.sin(planet_param.get("ano_plu"));
		cl +=  15517*Math.sin(2*planet_param.get("ano_plu"));
		cl -=   3593*Math.sin(2*planet_param.get("lat_plu"));
		cl +=   3414*Math.sin(3*planet_param.get("ano_plu"));
		cl -=   2201*Math.sin(planet_param.get("ano_plu")-2*planet_param.get("lat_plu"));
		cl -=   1871*Math.sin(planet_param.get("ano_plu")+2*planet_param.get("lat_plu"));
		cl +=    839*Math.sin(4*planet_param.get("ano_plu"));
		cl -=    757*Math.sin(2*planet_param.get("ano_plu")+2*planet_param.get("lat_plu"));
		cl -=    285*Math.sin(3*planet_param.get("ano_plu")+2*planet_param.get("lat_plu"));
		cl +=    227*time_param*time_param*Math.sin(planet_param.get("ano_plu"));
		cl +=    218*Math.sin(2*planet_param.get("ano_plu")-2*planet_param.get("lat_plu"));
		cl +=    200*time_param*Math.sin(planet_param.get("ano_plu"));
		planet_pos.put("cl", cl);

		double cb =57726*Math.sin(planet_param.get("lat_plu"));
		cb +=15257*Math.sin(planet_param.get("ano_plu")-planet_param.get("lat_plu"));
		cb +=14102*Math.sin(planet_param.get("ano_plu")+planet_param.get("lat_plu"));
		cb += 3870*Math.sin(2*planet_param.get("ano_plu")+planet_param.get("lat_plu"));
		cb += 1138*Math.sin(3*planet_param.get("ano_plu")+planet_param.get("lat_plu"));
		cb +=  472*Math.sin(2*planet_param.get("ano_plu")-planet_param.get("lat_plu"));
		cb +=  353*Math.sin(4*planet_param.get("ano_plu")+planet_param.get("lat_plu"));
		cb -=  144*Math.sin(planet_param.get("ano_plu")-3*planet_param.get("lat_plu"));
		cb -=  119*Math.sin(3*planet_param.get("lat_plu"));
		cb -=  111*Math.sin(planet_param.get("ano_plu")+3*planet_param.get("lat_plu"));
		planet_pos.put("cb", cb);

		double rv =40.74638;
		rv -= 9.58235*Math.cos(planet_param.get("ano_plu"));
		rv -= 1.16703*Math.cos(2*planet_param.get("ano_plu"));
		rv -= 0.22649*Math.cos(3*planet_param.get("ano_plu"));
		rv -= 0.04996*Math.cos(4*planet_param.get("ano_plu"));
		planet_pos.put("rv", rv);
	}

	/**************************************
	* pluto position cal sub 2            *
	*   input  : jd = julian day          *
	*   output : cl = longitude [sec,deg] *
	*            cb = latitude  [sec,deg] *
	*            rv = radius    [AU]      *
	**************************************/
	private void pluto_cal2(double jd) {
		double t = (jd - 2451545.0) / 36525;

		double cl =241.82574;
		cl +=179.09519 *t;
		cl += 15.81087 *cosD( 246.556453*t+298.348019);
		cl +=  1.18379 *cosD( 551.34710 *t+351.67676);
		cl +=  0.07886 *cosD( 941.622   *t+ 41.989);
		cl +=  0.00861 *cosD(2836.46    *t+ 60.35);
		cl +=  0.00590 *cosD(1306.75    *t+112.91);
		cl +=  0.00145 *cosD(2488.14    *t+ 19.01);
		cl +=  0.00022 *cosD(5861.8     *t+ 77.9);
		cl +=  0.00013 *cosD(3288.8     *t+293.0);
		cl *= 3600;
		planet_pos.put("cl", cl);

		double cb =17.04550 *cosD( 172.554318*t+ 42.574982);
		cb += 2.45310 *cosD( 415.60630 *t+ 66.15350);
		cb -= 2.30285;
		cb += 0.26775 *cosD( 713.1227  *t+105.0840);
		cb += 0.01855 *cosD(1089.202   *t+146.660);
		cb += 0.00119 *cosD(2658.22    *t+293.06);
		cb += 0.00098 *cosD(3055.6     *t+ 18.8);
		cb += 0.00090 *cosD(1532.6     *t+213.7);
		cb += 0.00042 *cosD(2342.3     *t+254.2);
		cb *= 3600;
		planet_pos.put("cb", cb);

		double rv =38.662489;
		rv += 8.670489   *cosD( 181.3383*t+198.4973);
		rv += 0.333884   *cosD( 475.963 *t+228.717);
		rv += 0.008426   *cosD( 909.8   *t+252.9);
		rv += 0.007619*t *cosD(1425.9   *t+ 31.0);
		rv += 0.004902   *cosD(2831.6   *t+149.4);
		rv += 0.002543*t *cosD(2196.1   *t+199.5);
		rv += 0.001188   *cosD(1748.0   *t+114.1);
		rv += 0.000390   *cosD(3188     *t+ 15);
		rv += 0.000116   *cosD(5860     *t+169);
		planet_pos.put("rv", rv);
	}


	double pcl[] = new double[22]; /* planet & casp celestial longitude */
	double pcb[] = new double[10]; /* planet celestial latitude */
	double pdm[] = new double[10]; /* planet dairy motion */
	double pdc[] = new double[10]; /* planet declination */


	/******************************************************
	* planet geocentric ecliptic position calculate sub   *
	*   input  : jd    = julian day                       *
	*   output : pcl[] = planet celestial longitude [rad] *
	*            pcb[] = planet celestial latitude  [rad] *
	******************************************************/
	private void planetCalc2(double jd) {
		double c[] = {
			0.0035, /* mercury */
			0.0048, /* venus */
			0.0071, /* mars */
			0.0129, /* jupiter */
			0.0174, /* saturn */
			0.0249, /* uranus */
			0.0312, /* neptune */
			0.0347  /* pluto */
		};
		double gcl = 0;
		double gcb = 0;
		double grv = 0;
		double sl=0;
		double sr=0;
		double sx=0;
		double sy=0;

		get_pr1(jd); /* planet parameter cal */

		for(int i=0; i<10; i++) {
			if(i==0) { /* sun position cal */
				sun_cal1();
				gcl = planet_pos.get("cl") / 1296000;
				gcl = (gcl - Math.floor(gcl)) * Math.PI * 2;
				gcb = planet_pos.get("cb");
				sl = gcl;
				sr = planet_pos.get("rv");
				sx = planet_pos.get("rv") * Math.cos(gcl);
				sy = planet_pos.get("rv") * Math.sin(gcl);
				gcl-=0.0057*Math.PI / 180; /* apparent longitude of sun */
			} else if(i==1) { /* moon position cal */
				moon_cal1();
				gcl = planet_pos.get("cl") * Math.PI / 648000;
				gcb = planet_pos.get("cb") * Math.PI / 648000;
			} else { /* planet position cal */
				if(i==2)      mercury_cal1();
				else if(i==3) venus_cal1();
				else if(i==4) mars_cal1();
				else if(i==5) jupiter_cal1();
				else if(i==6) saturn_cal1();
				else if(i==7) uranus_cal1();
				else if(i==8) neptune_cal1();
				else {
					if((jd > 2432384.28546) && (jd < 2479514.98634))
						pluto_cal2(jd);  /* 1947 - 2076 */
					else
						pluto_cal1();
				}
				planet_pos.put("cl", planet_pos.get("cl") / 1296000);
				planet_pos.put("cl", (planet_pos.get("cl") - Math.floor(planet_pos.get("cl"))) * Math.PI * 2);
				planet_pos.put("cb", planet_pos.get("cb") * Math.PI / 648000);

				/* change geoocentric */
				double x = planet_pos.get("rv") * Math.cos(planet_pos.get("cb")) * Math.cos(planet_pos.get("cl")) + sx;
				double y = planet_pos.get("rv") * Math.cos(planet_pos.get("cb")) * Math.sin(planet_pos.get("cl")) + sy;
				double z = planet_pos.get("rv") * Math.sin(planet_pos.get("cb"));
				grv = Math.sqrt( x * x +  y * y + z * z );
				gcb = Math.asin( z / grv );
				gcl = Math.atan2( y, x );
				gcl-= (0.0057 * Math.PI / 180) / sr * Math.cos(gcl - sl) + (c[i-2] * Math.PI / 180) / planet_pos.get("rv") * Math.cos(gcl - planet_pos.get("cl"));
			}
			gcl /= Math.PI * 2; /* longitude normalize */
			pcl[i] = (gcl - Math.floor(gcl)) * Math.PI * 2;
			pcb[i] = gcb;
		}
	}

	private void planetCalc1(double jd, double lat, double lon) {
		/********************************
		* obliquity of the ecliptic cal *
		********************************/
		double t = (jd - 2451545) / 36525;
		double e = 23.43928 - 0.01301 * t; /* obliquity of the ecliptic [rad] */
		e += 0.00256 * cosD(1934 * t + 235);
		e += 0.00016 * cosD(72002 * t + 201);
		e *= Math.PI / 180; /* e value [rad] */

		/*************************
		* local sideral time cal *
		*************************/
		t = jd - 2442412.5; /* from 1975 jan/0 0:0 */
		double tt = t - Math.floor(t);
		t /= 365.25;

		/* local sideral time [rad] */
		double st = 99.0361 + 360.00770 * t + 360 * tt; /* greenwich sideral time get */
		st += 0.0044 * sinD(-19.3 * t + 68.6);
		st += 0.0003 * sinD(  720 * t + 18);
		st = st * Math.PI / 180 + lon; /* local sideral time get */
		st /= Math.PI * 2; /* value normalize */
		st -= Math.floor(st);
		st *= Math.PI * 2; /* local sideral time value [rad] */

		/****************
		* ascendant cal *
		****************/
		double x = Math.cos(st + Math.PI / 2) * Math.cos(e) + Math.sin(e) / Math.tan(lat + Math.PI / 2);
		double y = Math.sin(st + Math.PI / 2);
		double z = 0;
		double asc = Math.atan2(y, x); /* ascendant celestial position [rad] */
		if (asc < 0)
			asc += Math.PI * 2;

		/*****************
		* house casp cal *
		*****************/
		pcl[10] = asc;				/* ascendant set */
		pcl[16] = asc + Math.PI;	/* descandant set */

		// $ra; /* right ascension [rad] */
		// $dc; /* declination     [rad] */
		// $ha; /* house angle     [rad] */
		for (int i=1; i<6; i++) {
			double ccl1 = pcl[i+9];			/* min angle set */
			double ccl2 = asc + Math.PI;	/* max angle */
			double ccle = i * Math.PI / 6;	/* target angle */

			double cl = 0;
			while(true) {
				cl = (ccl1 + ccl2) / 2;

				/* translate ecliptic to equatorial */
				x = Math.cos(cl);
				y = Math.sin(cl) * Math.cos(e);
				z = Math.sin(cl) * Math.sin(e);
				double ra = Math.atan2(y, x);
				if (ra < 0)
					ra += Math.PI * 2;
				double dc = Math.asin(z);

				/* translate equatorial to placidus house angle */
				double ct = -Math.tan(lat) * Math.tan(dc);
				if ((ct < -1) || (ct > 1)) {
					cl = Double.NaN;	/* cal impossible */
					break;
				}
				tt = Math.acos(ct);	/* rise & set cal */
				t = st - ra;		/* hour angle cal */
				if (t < -Math.PI)
					t += Math.PI * 2;
				else if (t > Math.PI)
					t -= Math.PI*2;

				double ha = 0;
				if ((t >= -tt) && (t <= tt)) {	/* above the horison */
					ha = Math.PI / 2 * (3 - t / tt);
				} else {					/* below the horison */
					t += Math.PI;
					if (t > Math.PI)
						t -= Math.PI * 2;
					ha = (1 - t / (Math.PI - tt)) * Math.PI / 2;
				}
				if (ha < 0)
					ha += Math.PI * 2; /* house angle normalize */
				else if(ha >= Math.PI * 2)
					ha -= Math.PI * 2;
				ha -= ccle;
				if(ha > 0)
					ccl2 = cl;
				else
					ccl1 = cl;
				if(Math.abs(ha) < 0.000001)
					break;
			}

			pcl[i+10] = cl;
			pcl[i+16] = cl + Math.PI;
		}
		for (int i=10; i<22; i++) {
			if (pcl[i] >= Math.PI * 2)
				pcl[i] -= Math.PI * 2;
			pcl[i] = Math.floor(pcl[i] * 10800 / Math.PI);
		}

		/***************************
		* planets dairy motion cal *
		***************************/
		planetCalc2(jd + 0.5);
		for (int i=0; i<10; i++) {
			pdm[i] = pcl[i];
		}
		planetCalc2(jd - 0.5);
		for (int i=0; i<10; i++) {
			pdm[i] -= pcl[i];
			if (pdm[i] > Math.PI)
				pdm[i] -= Math.PI * 2;
			else if(pdm[i] < -Math.PI)
				pdm[i] += Math.PI * 2;
		}

		/***********************
		* planets position cal *
		***********************/
		planetCalc2(jd);
		for (int i=0; i<10; i++)	{
			double cl = pcl[i];
			double cb = pcb[i];

			/* translate ecliptic to equatorial */
			z = Math.sin(cb) * Math.cos(e) + Math.cos(cb) * Math.sin(cl) * Math.sin(e);
			double dc = Math.asin(z);

			pcl[i] = Math.floor(cl * 10800 / Math.PI);
			pdc[i] = Math.floor(dc * 10800 / Math.PI);
		}

	}

	private double julianDayCalc(int _iYear, int _iMonth, int iDay, int iHour, int iMinute) {
		int iYear  = _iYear;
		int iMonth = _iMonth;
		/*****************
		* julian day cal *
		*****************/
		if (iMonth < 3) {
			iYear -= 1;
			iMonth += 12;
		}
		double t =  Math.floor(iYear / 100);
		double jd = Math.floor(iYear * 365.25) - t + Math.floor(t / 4);
		jd += Math.floor(30.6001 * (iMonth + 1)) + iDay + 1720996.5;
		jd += (double)(iHour) / (double)(24.0) + (double)(iMinute) / (double)(1440.0);
		jd -= 9.0 / 24.0;
		return jd;
	}

	private double[] myLonLat(int place) {
		double rtn[] = {0, 0};
		rtn[0] = PlaceLon[(place <= 0 ? DEFAULT_PLACE : place )] / 10800 * Math.PI;
		rtn[1] = PlaceLat[(place <= 0 ? DEFAULT_PLACE : place )] / 10800 * Math.PI;
		return rtn;
	}

	public double[][] calculate(Calendar day, int place) {
		double jd = julianDayCalc(day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DATE), day.get(Calendar.HOUR_OF_DAY), day.get(Calendar.MINUTE));
		double lon_lat[] = myLonLat(place);
		planetCalc1(jd, lon_lat[0], lon_lat[1]);
		double rtn[][] = {pcl, pdc};
		return rtn;
	}

	public double[][] planet_pos(Calendar day, int place) {
		double lon_lat[] = myLonLat(place);
		return planet_pos(day, lon_lat[0], lon_lat[1]);
	}

	public double[][] planet_pos(Calendar day, double latitude, double longitude) {
		Log.d(TAG, "Lat=[" + latitude + "] Lon=[" + longitude + "]");
		double jd = julianDayCalc(day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DATE), day.get(Calendar.HOUR_OF_DAY), day.get(Calendar.MINUTE));
		planetCalc1(jd, latitude, longitude);

		// 0:太陽 1:月 2:水星 3:金星 4:火星 5:木星 6:土星 7:天王星 8:海王星 9:冥王星 10:ASC
		double rtn[][] = new double[11][2];		// 太陽 ～ 冥王星
//		si:
//		 0="おひつじ座";	 1="おうし座";		 2="ふたご座";
//		 3="かに座";		 4="しし座";		 5="おとめ座";
//		 6="てんびん座";	 7="さそり座";		 8="いて座";
//		 9="やぎ座";		10="みずがめ座";	11="うお座";
		for (int i=0; i<rtn.length; i++) {
			double mm = pcl[i];
			double si = Math.floor(mm / 1800);
			mm -= si * 1800;
			double dd = Math.floor(mm / 60);
			mm -= dd * 60;

			mm /= 100;
			if (i < pdm.length && pdm[i] < 0) {
				dd *= -1;
				mm *= -1;
			}
			rtn[i][0] = si;
			rtn[i][1] = dd + mm;
			//Log.d(TAG, "i=" + i + " pcl=" + pcl[i]+ " si=" + si + " DD.MM=" + (dd + mm));
		}

		return rtn;
	}

	private double[] aspectOrbs() {
		/******************
		* Get aspect orbs *
		******************/
		double asp_def[] = {0.0, 5.0, 1.5, 1.5, 3.0, 1.5, 3.0, 3.0, 1.5, 1.5, 3.0, 1.0};
		double dAsp[] = new double[12]; /* aspect orbs value [deg,min] */
		for (int i=0; i<asp_def.length; i++) {
			dAsp[i] = Math.floor(asp_def[i] * 60);
		}
		return dAsp;
	}

	public double[][][] execute(Calendar birthDay, int place, Calendar targetDate) {
		double pcl_rel[][] = new double[pcl.length][2];
		double pdc_rel[][] = new double[pdc.length][2];

		Calendar dates[] = {birthDay, targetDate};
		double lon_lat[] = myLonLat(place);
		for (int i=0; i<dates.length; i++) {
			double jd = julianDayCalc(dates[i].get(Calendar.YEAR), dates[i].get(Calendar.MONTH) + 1, dates[i].get(Calendar.DATE), dates[i].get(Calendar.HOUR_OF_DAY), dates[i].get(Calendar.MINUTE));
			planetCalc1(jd, lon_lat[0], lon_lat[1]);
			pcl_rel[i] = pcl;
			pdc_rel[i] = pdc;
		}

		double dAsps[] = aspectOrbs();
		double planet_rel[][][] = new double[10][10][2];
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				double mm = Math.abs(pcl_rel[0][i] - pcl_rel[1][j]); /* aspect angle cal */
				if (mm > 180*60)
					mm = 360*60 - mm;
				double si = 0;
				if (              mm        <= dAsps[ 1])
					si= 1; /*   0 */
				else if (Math.abs(mm- 30*60)<= dAsps[ 2])
					si= 2; /*  30 */
				else if (Math.abs(mm- 45*60)<= dAsps[ 3])
					si= 3; /*  45 */
				else if (Math.abs(mm- 60*60)<= dAsps[ 4])
					si= 4; /*  60 */
				else if (Math.abs(mm- 72*60)<= dAsps[ 5])
					si= 5; /*  72 */
				else if (Math.abs(mm- 90*60)<= dAsps[ 6])
					si= 6; /*  90 */
				else if (Math.abs(mm-120*60)<= dAsps[ 7])
					si= 7; /* 120 */
				else if (Math.abs(mm-135*60)<= dAsps[ 8])
					si= 8; /* 135 */
				else if (Math.abs(mm-150*60)<= dAsps[ 9])
					si= 9; /* 150 */
				else if (       -(mm-180*60)<= dAsps[10])
					si=10; /* 180 */
				else if (Math.abs(pdc_rel[0][i]-pdc_rel[1][j])<= dAsps[11])
					si=11; /* parallel */
				else
					si=0;
				planet_rel[i][j][0] = si;

				double dd = Math.floor(mm/60);
				mm = Math.floor((mm-dd*60)/6);
				planet_rel[i][j][1] = Double.parseDouble( dd  + "." + mm );
			}
		}
		return planet_rel;
	}

} // End of Class [PlanetCalc]
