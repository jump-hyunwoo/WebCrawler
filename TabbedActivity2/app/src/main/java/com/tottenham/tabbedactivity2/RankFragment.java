package com.tottenham.tabbedactivity2;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends Fragment {


    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rank, container, false);
        //new EPLRanking().execute();

        //  update  버튼을 누르면 실시간으로 정보를 업데이트하여 새로운 화면을 띄우게 된다.
        Button btn = (Button)rootView.findViewById(R.id.button_update_rank);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EPLRanking().execute();
            }
        });

        return rootView;
    }

    private class EPLRanking extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private String[][] rankInformation = {{"","","","",""},{"","","","",""},{"","","","",""}, {"","","","",""},{"","","","",""},{"","","","",""},
                {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},
                {"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""},{"","","","",""}};//20 X 5 의 2차원 배열을 이용하면 편할 것이다.

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("경기 일정을 가져오고 있습니다."); //리그 순위를 가져오는 메시지가 경기 일정을 가져오는 메시지를 덮어써서 문제가 됨, 따라서 하나로 통일함
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty" +
                        "&fbm=1&ie=utf8&query=epl+%EC%88%9C%EC%9C%84").get();

                Elements topTeams = doc.select("td[class=team tp wdt]");
                Elements nomalTeams = doc.select("td[class=team wdt]");
                Elements informations = doc.select("td[class=tp]");

                for(int i = 0; i < 20; i++) rankInformation[i][0] = Integer.toString(i+1); // 순위를 계산하는 과정

                String[] topTeam = topTeams.text().split(" "); // 이름을 가져오는 과정 (1위 팀)
                rankInformation[0][1] = topTeam[0];

                String[] nomalTeam = nomalTeams.text().split(" "); // 이름을 가져오는 과정 (나머지 팀)
                for(int i = 0; i < 19; i++) {
                    rankInformation[i+1][1] = nomalTeam[i];
                }

                String[] information = informations.text().split(" ");
                for(int i = 0; i < 20; i++) {
                    rankInformation[i][2] = information[8 * i]; //경기를 가져오는 식
                    rankInformation[i][3] = information[8 * i + 1]; //승점을 가져오는 식
                    rankInformation[i][4] = information[8 * i + 7]; //득실차를 가져오는 식
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int[][] idArray = {{R.id.rank1,R.id.team1,R.id.match1,R.id.point1,R.id.goal_difference1},{R.id.rank2,R.id.team2,R.id.match2,R.id.point2,R.id.goal_difference2},
                    {R.id.rank3,R.id.team3,R.id.match3,R.id.point3,R.id.goal_difference3},{R.id.rank4,R.id.team4,R.id.match4,R.id.point4,R.id.goal_difference4},
                    {R.id.rank5,R.id.team5,R.id.match5,R.id.point5,R.id.goal_difference5},{R.id.rank6,R.id.team6,R.id.match6,R.id.point6,R.id.goal_difference6},
                    {R.id.rank7,R.id.team7,R.id.match7,R.id.point7,R.id.goal_difference7},{R.id.rank8,R.id.team8,R.id.match8,R.id.point8,R.id.goal_difference8},
                    {R.id.rank9,R.id.team9,R.id.match9,R.id.point9,R.id.goal_difference9},{R.id.rank10,R.id.team10,R.id.match10,R.id.point10,R.id.goal_difference10},
                    {R.id.rank11,R.id.team11,R.id.match11,R.id.point11,R.id.goal_difference11},
                    {R.id.rank12,R.id.team12,R.id.match12,R.id.point12,R.id.goal_difference12},{R.id.rank13,R.id.team13,R.id.match13,R.id.point13,R.id.goal_difference13},
                    {R.id.rank14,R.id.team14,R.id.match14,R.id.point14,R.id.goal_difference14},{R.id.rank15,R.id.team15,R.id.match15,R.id.point15,R.id.goal_difference15},
                    {R.id.rank16,R.id.team16,R.id.match16,R.id.point16,R.id.goal_difference16},{R.id.rank17,R.id.team17,R.id.match17,R.id.point17,R.id.goal_difference17},
                    {R.id.rank18,R.id.team18,R.id.match18,R.id.point18,R.id.goal_difference18},{R.id.rank19,R.id.team19,R.id.match19,R.id.point19,R.id.goal_difference19},
                    {R.id.rank20,R.id.team20,R.id.match20,R.id.point20,R.id.goal_difference20}}; //20 X 5 배열로 선언하여 바로 사상하여 사용하도록 한다.

            TextView[][] textViewArray = new TextView[20][5];

            for(int i = 0; i < 20; i++) {
                for(int j = 0; j < 5; j++) {
                    textViewArray[i][j] = (TextView)getView().findViewById(idArray[i][j]);
                    textViewArray[i][j].setText(rankInformation[i][j]);
                }
            }

            progressDialog.dismiss();
        }
    }

}
