package com.tottenham.tabbedactivity2;


import android.app.ProgressDialog;
import android.graphics.Color;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchFragment extends Fragment {


    public MatchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);
        //new MatchScheduler().execute();

        //  update  버튼을 누르면 실시간으로 정보를 업데이트하여 새로운 화면을 띄우게 된다.
        Button btn = (Button)rootView.findViewById(R.id.button_update_match);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MatchScheduler().execute();
            }
        });

        return rootView;
    }

    private class MatchScheduler extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private String[] firstGameStr = {"","","","",""}; //date, team, stadium, time, (---)
        private String[] secondGameStr = {"","","","",""};
        private String test;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("경기 일정을 가져오고 있습니다.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                /*
                Document 클래스는 연결해서 얻어온 HTML 전체 문서
                Element 클래스는 Document의 HTML 요소
                Elements 클래스는 Element가 모인 자료형
                */

                Document doc;
                doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=" +
                        "%ED%86%A0%ED%8A%B8%EB%84%98+%EA%B2%BD%EA%B8%B0%EC%9D%BC%EC%A0%95").get();

                Elements selected = doc.select("tr[class=_selected]").select("th[colspan=7]"); //_selected 클래스는 오늘 경기를 가리켜준다.
                Elements dates = doc.select("th[colspan=7]");
                Elements leftTeams = doc.select("td[class=l_team]");
                Elements rightTeams = doc.select("td[class=r_team]");
                Elements scores = doc.select("td[class=score]");
                Elements stadiums = doc.select("span[class=stadium_ellipsis]");
                Elements times = doc.select("span[class=bg_none]");

                String selectedDate = selected.text();//selected를 통해 해당 날짜의 인덱스 값을 구한다.
                int index = 0;
                for(Element date : dates) {
                    if(selectedDate.equals(date.text())) break;
                    index++;
                }

                firstGameStr[0] = dates.get(index).text();//구한 인덱스로 Elements에 접근한 후 원하는 데이터만 연결해준다.
                firstGameStr[1] = leftTeams.get(index).text() + " " + scores.get(index).text() + " " + rightTeams.get(index).text();
                firstGameStr[2] = stadiums.get(index).text();
                firstGameStr[3] = times.get(index).text();

                if(index+1 != dates.size()) {
                    secondGameStr[0] = dates.get(index+1).text();
                    secondGameStr[1] = leftTeams.get(index+1).text() + " " + scores.get(index+1).text() + " " + rightTeams.get(index+1).text();
                    secondGameStr[2] = stadiums.get(index+1).text();
                    secondGameStr[3] = times.get(index+1).text();
                }

                else if(index+1 == dates.size()) {//selected가 달의 마지막 경기일 경우
                    String newUrl = doc.select("span[class=nx]").select("a[href]").attr("abs:href");

                    doc = Jsoup.connect(newUrl).get();

                    Elements newSelected = doc.select("tr[class=_selected]").select("th[colspan=7]"); //_selected 클래스는 오늘 경기를 가리켜준다.
                    Elements newDates = doc.select("th[colspan=7]");
                    Elements newLeftTeams = doc.select("td[class=l_team]");
                    Elements newRightTeams = doc.select("td[class=r_team]");
                    Elements newScores = doc.select("td[class=score]");
                    Elements newStadiums = doc.select("span[class=stadium_ellipsis]");
                    Elements newTimes = doc.select("span[class=bg_none]");

                    secondGameStr[0] = newDates.get(0).text();
                    secondGameStr[1] = newLeftTeams.get(0).text() + " " + newScores.get(0).text() + " " + newRightTeams.get(0).text();
                    secondGameStr[2] = newStadiums.get(0).text();
                    secondGameStr[3] = newTimes.get(0).text();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView tv0 = (TextView)getView().findViewById(R.id.first_match_date);
            tv0.setText(firstGameStr[0]);

            TextView tv1 = (TextView)getView().findViewById(R.id.first_match_team);
            tv1.setText(firstGameStr[1]);

            TextView tv2 = (TextView)getView().findViewById(R.id.first_match_stadium);
            tv2.setText(firstGameStr[2]);
            if(firstGameStr[2].equals("Tottenham Hotspur Stadium")) tv2.setTextColor(Color.parseColor("#202060"));

            TextView tv3 = (TextView)getView().findViewById(R.id.first_match_time);
            tv3.setText(firstGameStr[3]);

            TextView tv4 = (TextView)getView().findViewById(R.id.second_match_date);
            tv4.setText(secondGameStr[0]);

            TextView tv5 = (TextView)getView().findViewById(R.id.second_match_team);
            tv5.setText(secondGameStr[1]);

            TextView tv6 = (TextView)getView().findViewById(R.id.second_match_stadium);
            tv6.setText(secondGameStr[2]);
            if(secondGameStr[2].equals("Tottenham Hotspur Stadium")) tv6.setTextColor(Color.parseColor("#202060"));

            TextView tv7 = (TextView)getView().findViewById(R.id.second_match_time);
            tv7.setText(secondGameStr[3]);

            progressDialog.dismiss();
        }
    }
}

