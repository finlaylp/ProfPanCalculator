
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;


public class LiquidTempCal extends JApplet implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTabbedPane pane = new JTabbedPane(JTabbedPane.LEFT);
	JLabel top1,top2; // title for BGA texts
	JLabel ta1, ta2, ta3, ta4, ta5, b1; // package dimension texts (left side)
	JLabel sd1,sd2,sd3,sd4,sd5,sd6; // stencil dimension texts (center)
	JLabel sp1, sp2, sp3, sp4, sp5, sp6;
	JTextField pitchMils, ballD; // package dimension fields;
	JTextField apSize, stenThick, transRat; // stencil dimension fields
	JTextField sn, pb, ag, mcp, cu;
	JTextField rSn, rPb, rAg, rCu, rTemp, rSolVol;
	JLabel r1,r2,r3,r4,r5,r6,r7,r8,r9;
	JLabel label1, label2, label3,blank;
	
	JLabel f1,f2,f3,f4;
	JTextField t1, t2, t3;
	JButton calc, clear, defaultButton;
	

	
	JComboBox ballType;
	
	JComboBox pitchMm;
	JComboBox apShape;
	JButton silver, noSilver, silverCu;
	JPanel bga, termination, lead;
	JPanel title, left, center, right, bottom1, bottom2, bottom3,ball, clearBut;
	JPanel calcBut;
	JTextArea ter1, lead1; // Coming Soon
	
	double pitch, diameter,solSn, solCu, solPb , solAg, metalContent, length, thickness, ratio, shape;
	double ballVolume, pasteVolume, perPb, perAg, perSn, perCu, calcTemp;
	double density, densitySnAgCu, ballSn, ballAg, ballCu;
	boolean isSquare;
	int pasteSelect;
	String pPb, pAg, pSn, pCu, cTemp, sVol;
	
	public void init(){
		//setLayout(new BorderLayout());
		b1 = new JLabel("");
		bga = new JPanel(new GridLayout (4,2));
		setupPackage();
		setupStencil();
		setupSolder();
		setupBall();
		setupCalc();
		setupClear();
		
		setupResults();
		setupResults2();
		
		
		ter1 = new JTextArea ("Under \nConstruction");
		ter1.setEditable(false);
		ter1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		
		lead1 = new JTextArea ("Under \nConstruction");
		lead1.setEditable(false);
		lead1.setFont(new Font("Times New Roman", Font.BOLD, 15));
		
		termination = new JPanel(new FlowLayout());
		termination.add(ter1);	
		
		
		lead = new JPanel (new FlowLayout());
		lead.add(lead1);
		
		pane.add("BGA / CSP", bga);
		pane.add("Termination", termination);
		pane.add("Lead Frame", lead);
		add(pane);
	}
	// Package Dimension
	public void setupPackage(){
		left = new JPanel (new FlowLayout());
		label1 = new JLabel ("Input");
		label1.setFont((new Font ("Arial", Font.BOLD, 20)));
		label1.setAlignmentX(LEFT_ALIGNMENT);
		ta1 = new JLabel ("          Package Dimensions          ");
		ta1.setFont(new Font ("Arial", Font.BOLD, 14));
		ta2 = new JLabel ("Pitch (mm):");
		ta3 = new JLabel ("     Pitch (mils):");
		ta4 = new JLabel ("Ball Diameter \n (mils):");
		ta5 = new JLabel ("*Note: 1mm equals roughly 40 mils");
		ta5.setFont(new Font ("Arial", Font.PLAIN, 10));
		left.add (ta1);
		pitchMils = new JTextField (4);
		ballD = new JTextField (4);
		pitchMm = new JComboBox ();
		pitchMm.addItem("");
		pitchMm.addItem(0.5);
		pitchMm.addItem(0.65);
		pitchMm.addItem(0.8);
		pitchMm.addItem(1.0);
		pitchMm.addItem(1.27);
		pitchMm.setEditable(false);
		pitchMm.addActionListener(this);
		defaultButton = new JButton ("Default");
		defaultButton.addActionListener(this);
		left.add(label1);
		left.add(ta1);
		left.add(ta2);
		left.add(pitchMm);
		left.add(defaultButton);
		left.add(ta3);
		left.add(pitchMils);
		left.add(ta4);
		left.add(ballD);
		left.add(ta5);
		bga.add(left);
		
	}
	//Stencil Dimensions
	public void setupStencil(){
		center = new JPanel (new FlowLayout());
		blank = new JLabel("                          ");
		blank.setFont(new Font ("Arial", Font.PLAIN, 20 ));
		sd1 = new JLabel("     Stencil Dimensions     ");
		sd1.setFont(new Font ("Arial", Font.BOLD, 14));
		sd2 = new JLabel ("Aperture Shape: ");

		sd3 = new JLabel ("Aperture Size (mils):");
	
		sd4 = new JLabel ("Stencil Thickness (mils):");
	
		sd5 = new JLabel ("Transfer Ratio (in %): ");

		
		apSize = new JTextField (5);
		stenThick = new JTextField (5);
		transRat = new JTextField (5);
		apShape = new JComboBox();
		
		apShape.addItem("");
		apShape.addItem("Square");
		apShape.addItem("Round");
		apShape.addActionListener(this);
		center.add(blank);
		center.add(sd1);
		center.add(sd2);
		center.add(apShape);
		center.add(sd3);
		center.add(apSize);
		center.add(sd4);
		center.add(stenThick);
		center.add(sd5);
		center.add(transRat);
		
		bga.add(center);
		
		
	}
	
	public void setupSolder(){
		JLabel space3 = new JLabel ("            ");
		JLabel space4 = new JLabel ("            ");
		JLabel space5 = new JLabel ("            ");
		right = new JPanel (new FlowLayout());
		silver = new JButton("SnPbAg");
		noSilver = new JButton ("SnPb");
		silverCu = new JButton ("SnAgCu");
		silver.addActionListener(this);
		noSilver.addActionListener(this);
		silverCu.addActionListener(this);
		sp1 = new JLabel ("          Solder Paste          ");
		sp1.setFont(new Font ("Arial", Font.BOLD, 14));
		sp2 = new JLabel ("            Sn (in %): ");
		sp3 = new JLabel ("            Pb (in %): ");
		sp4 = new JLabel ("            Ag (in %): ");
		sp5 = new JLabel ("Metal Content in Paste (% in volume): ");
		sp6 = new JLabel ("Cu (in %): ");
		sn = new JTextField (5);
		pb = new JTextField (5);
		ag = new JTextField (5);
		mcp = new JTextField (5);
		cu = new JTextField (5);
		
			
		right.add(sp1);
		right.add(silver);
		right.add(noSilver);
		right.add(silverCu);
		right.add(sp2);
		right.add(sn);
		right.add(space3);
		right.add(sp3);
		right.add(pb);
		right.add(space4);
		right.add(sp4);
		right.add(ag);
		right.add(space5);
		right.add(sp6);
		right.add(cu);
		right.add(sp5);
		right.add(mcp);
		
		
		bga.add(right);
		
		
	}
	
	public void setupBall(){
		JLabel space = new JLabel ("         ");
		JLabel space2 = new JLabel ("            ");
		ball = new JPanel (new FlowLayout ());
		f1 = new JLabel ("                 Solder Ball                 ");
		f1.setFont(new Font ("Arial", Font.BOLD, 14));
		f2 = new JLabel ("         Sn (in %): ");
		f3 = new JLabel ("            Ag (in %): ");
		f4 = new JLabel ("Cu (in %): ");
		t1 = new JTextField ("", 5);
		t2 = new JTextField ("", 5);
		t3 = new JTextField ("", 5);
		
		
		ball.add(f1);
		ball.add(f2);
		ball.add(t1);
		ball.add(space);
		ball.add(f3);
		ball.add(t2);
		ball.add(space2);
		ball.add(f4);
		ball.add(t3);


		
		
		bga.add(ball);
	}
	
	public void setupCalc(){
		calcBut = new JPanel (new FlowLayout( FlowLayout.RIGHT));
		calc = new JButton ("Calculate");
		calc.addActionListener (this);
		blank = new JLabel ("                                     ");
		blank.setFont(new Font("Arial", Font.PLAIN, 60));
		
		calcBut.add(blank);
		calcBut.add(calc);
		
		bga.add(calcBut);
	}
	
	public void setupClear(){
		clearBut = new JPanel (new FlowLayout( FlowLayout.LEFT));
		clear = new JButton ("Clear");
		clear.addActionListener (this);
		blank = new JLabel ("                                     ");
		blank.setFont(new Font("Arial", Font.PLAIN, 60));
		
		clearBut.add(blank);
		
		clearBut.add(clear);
		
		bga.add(clearBut);
	}
	public void setupResults(){
		JLabel space6 = new JLabel ("   ");
		bottom1 = new JPanel (new FlowLayout());
		r1 = new JLabel ("          Calculation Results           ");
		r1.setFont(new Font ("Arial", Font.BOLD, 20));
		r9 = new JLabel ("                     % of Mixed:                         ");
		r2 = new JLabel ("%Pb ");
		r3 = new JLabel ("%Ag ");
		r4 = new JLabel ("%Sn ");
		r5 = new JLabel ("%Cu ");
		rPb = new JTextField (5);
		rAg = new JTextField (5);
		rSn = new JTextField (5);
		rCu = new JTextField (5);
		bottom1.add(r1);
		bottom1.add (r9);
		bottom1.add (r2);
		bottom1.add (rPb);
		bottom1.add (r3);
		bottom1.add (rAg);
		bottom1.add (space6);
		bottom1.add (r4);
		bottom1.add (rSn);
		bottom1.add (r5);
		bottom1.add (rCu);

		
				
		bga.add(bottom1);
		
	}
	
	public void setupResults2(){
		blank = new JLabel("                          ");
		blank.setFont(new Font ("Arial", Font.PLAIN, 20 ));
		bottom2 = new JPanel (new FlowLayout());
                //bottom3 = new JPanel (new FlowLayout());
		r6 = new JLabel ("Liquid Temperature: ");
		r7 = new JLabel ("Celsius ");
		r8 = new JLabel ("Solder Vol. (mil ^3): ");
		rTemp = new JTextField (10);
		rSolVol = new JTextField (10);

		label3 = new JLabel ("Copyright 2008 (c) Jianbiao (John) Pan");
		label3.setFont((new Font ("Arial", Font.BOLD, 12)));
		label3.setAlignmentX(LEFT_ALIGNMENT);
		
		bottom2.add(blank);
		bottom2.add (r6);
		bottom2.add (rTemp);
		bottom2.add (r7);
		bottom2.add (r8);
		bottom2.add (rSolVol);

		bottom2.add(blank);
		bottom2.add(blank);
		bottom2.add(blank);

		bottom2.add (label3);
		
		bga.add(bottom2);
		//bga.add(bottom3);
		
	}
	
	 
	
	public void actionPerformed (ActionEvent ae){
		Object obj = ae.getSource();
		Object obj2 = ae.getSource();
		Object obj1 = ae.getSource();
		// converts mm to mils and calculate the ball diameter
		int index = pitchMm.getSelectedIndex();
		if (obj1 == defaultButton ){
			if (index == 1){
				pitchMils.setText("20");
				ballD.setText("10");
				apSize.setText("11");
				stenThick.setText("5");
				transRat.setText("70");
			}else if (index == 2){
				pitchMils.setText("25");
				ballD.setText("10");
				apSize.setText("14");
				stenThick.setText("5");
				transRat.setText("80");
			}else if (index == 3){
				pitchMils.setText("30");
				ballD.setText("14");
				apSize.setText("15");
				stenThick.setText("5");
				transRat.setText("85");
			}else if (index == 4){
				pitchMils.setText("40");
				ballD.setText("22");
				apSize.setText("17");
				stenThick.setText("5");
				transRat.setText("90");
			}else if (index == 5){
				pitchMils.setText("50");
				ballD.setText("28");
				apSize.setText("21");
				stenThick.setText("5");
				transRat.setText("100");
			}
		}
		
		if (obj == silver){ 
			ag.setEnabled(true);
			cu.setEnabled(false);
			pb.setEnabled(true);
				sn.setText("62");
				pb.setText("36");
				ag.setText("2");
				cu.setText("");
				mcp.setText("50");
			pasteSelect = 0;
		}else if (obj == noSilver){
			ag.setEnabled(false);
			cu.setEnabled(false);
			pb.setEnabled(true);
				sn.setText("63");
				pb.setText("37");
				ag.setText("");
				cu.setText("");
				mcp.setText("50");
				pasteSelect = 1;
		}else if (obj == silverCu){
			ag.setEnabled(true);
			cu.setEnabled(true);
			pb.setEnabled(false);
			sn.setText("96.5");
			ag.setText("3.0");
			cu.setText("0.5");
			pb.setText("");
			mcp.setText("50");
			pasteSelect = 2;
		}else if (obj2 == calc){
			try{
				pitch = Double.parseDouble(pitchMils.getText());
				diameter = Double.parseDouble(ballD.getText());
				solSn = Double.parseDouble(sn.getText());
				ballSn = Double.parseDouble(t1.getText());
				ballCu = Double.parseDouble(t3.getText()) ;
				ballAg = Double.parseDouble(t2.getText()) ;
				metalContent = Double.parseDouble(mcp.getText()) / 100;
				length = Double.parseDouble(apSize.getText());
				thickness = Double.parseDouble(stenThick.getText());
				ratio = Double.parseDouble(transRat.getText()) / 100;
				if (pasteSelect == 0){ 
					solSn = Double.parseDouble(sn.getText());
					solAg = Double.parseDouble(ag.getText());
					solPb = Double.parseDouble(pb.getText()) /100 ;
				}else if (pasteSelect == 2){
					solSn = Double.parseDouble(sn.getText());
					solAg = Double.parseDouble(ag.getText());
					solCu = Double.parseDouble(cu.getText());
				}else {
					solSn = Double.parseDouble(sn.getText());
					solPb = Double.parseDouble(pb.getText()) /100 ;
				}
				
			}catch (NumberFormatException e){
				
			}
			
					
			//ballVolume, pasteVolume, perPb, perAg, perSn, perCu, calcTemp
			
			double d;
			d = diameter / 2.00;
			ballVolume =  d * d * d * 3.14 * 4 / 3; 
			double bs, ba,bc;
			bs = ballSn / 100;
			ba = ballAg / 100;
			bc = ballCu / 100;
			densitySnAgCu = 1/ ((bs / 7.29) + (ba / 10.49) + (bc /8.92 ));
			double s, a, p;
			s = solSn / 100;
			p = solPb;
			if (pasteSelect == 0){
				a = solAg /100;
				density = 1 / ((s / 7.29) + (p / 11.34) + (a / 10.49));
			}else {
				density = 1 / ((s / 7.29) + (p / 11.34));
			}
			
			if (apShape.getSelectedIndex() == 1){
			
				pasteVolume = length * length * thickness * ratio;
				perPb = (solPb * pasteVolume * density * metalContent) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu) * 100;
				if (pasteSelect == 0){
					perCu = (ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
					perAg = (solAg * pasteVolume * density * metalContent + ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}else if (pasteSelect == 2){
					perAg = (solAg * pasteVolume * density * metalContent + ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
					perCu = (solCu * pasteVolume * density * metalContent + ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}else{
						perAg = (ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
						perCu = (ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}
				perSn = 100 - perPb - perAg - perCu;
			
				if (perAg > 3.5){
					calcTemp = 232 - (1.3 * perPb) - (3.1 * perAg) - (7.9 * perCu) + 5;
				}else {
					calcTemp = 232 - (1.3 * perPb) - (3.1 * perAg) - (7.9 * perCu);
				}
			
			}else if (apShape.getSelectedIndex() == 2 ){
				pasteVolume = 3.14 * Math.pow(length / 2, 2) * thickness * ratio;
				perPb = (solPb * pasteVolume * density * metalContent) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu) *100;
				if (pasteSelect == 0){
					perCu = (ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
					perAg = (solAg * pasteVolume * density * metalContent + ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}else if (pasteSelect == 2){
					perAg = (solAg * pasteVolume * density * metalContent + ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
					perCu = (solCu * pasteVolume * density * metalContent + ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}else{
						perAg = (ballAg * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
						perCu = (ballCu * ballVolume * densitySnAgCu) / (pasteVolume * density * metalContent + ballVolume * densitySnAgCu);
				}
				perSn = 100 - perPb - perAg - perCu;
			
				if (perAg > 3.5){
					calcTemp = 232 - (1.3 * perPb) - (3.1 * perAg) - (7.9 * perCu) + 5;
				}else {
					calcTemp = 232 - (1.3 * perPb) - (3.1 * perAg) - (7.9 * perCu);
				}
			}
			
			
			rPb.setText(String.format("%.2f", perPb));
			rAg.setText(String.format("%.2f", perAg));
			rCu.setText(String.format("%.2f", perCu));
			rSn.setText(String.format("%.2f", perSn));
			rTemp.setText(String.format("%.2f", calcTemp));
			rSolVol .setText(String.format("%.2f", pasteVolume));
			
			System.out.println("BallD: " + diameter);
			System.out.println("solPb: " + solPb);
			System.out.println("solAg: " + solAg);
			System.out.println("solSn: " + solSn);
			System.out.println("ballSn: " + ballSn);
			System.out.println("ballAg: " + ballAg);
			System.out.println("ballCu: " + ballCu);
			System.out.println("MetalCont: " + metalContent);
			System.out.println("perPb: " + perPb);
			System.out.println("perAg: " + perAg);
			System.out.println("perSn: " + perSn);
			System.out.println("perCu: " + perCu);
			System.out.println("ballVolume: " + ballVolume);
			System.out.println("pasteVolume: " + pasteVolume);
			System.out.println("solDensity: " + density);
			System.out.println("ballDensity: " + densitySnAgCu);
			System.out.println("ballVolume: " + ballVolume);
			
			solPb = 0;			
			
			
		}else if (obj2 == clear) {
			pitchMm.setSelectedIndex(0);
			pitchMils.setText("");
			ballD.setText("");
			apShape.setSelectedIndex(0);
			apSize.setText("");
			stenThick.setText("");
			transRat.setText("");
			silver.setSelected(true);
			sn.setText("");
			pb.setText("");
			ag.setText("");
			mcp.setText("");
			rPb.setText("");
			rAg.setText("");
			rSn.setText("");
			rCu.setText("");
			rTemp.setText("");
			rSolVol.setText("");
			t1.setText("");
			t2.setText("");
			t3.setText("");
			pitch = 0;
			diameter = 0;
			solSn = 0;
			solPb =0;
			solCu = 0;
			solAg =0;
			metalContent = 0;
			length =0;
			thickness =0;
			ratio = 0;
			
			
		}
		
		
	}

	


}

	

