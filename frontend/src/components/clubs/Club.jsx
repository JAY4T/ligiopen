// src/clubs/club-pages/AFCLeopards.jsx
import React from "react";
import ClubNavbar from "./ClubNav";
import Tabs from "../ReusableTab";
import PositionSection from "./PositionSection";
import TechnicalStaff from "./TechnicalStaff";
import ClubFooter from "./ClubFooter";

function Club() {
  const goalkeepers = [
    { number: 1, name: 'Yuri Lodygin', position: 'Goalkeeper', image: 'https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/kahata.png?raw=true' },
    { number: 69, name: 'Bartlomiej Dragowski', position: 'Goalkeeper', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/c4dd21e977d04c277ac49ef75177aaa05ff1bd6e/john.png' },
    { number: 81, name: 'Klidman Lilo', position: 'Goalkeeper', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/d548142d4e4083c57257b5a26f5faa76cbefc66f/ochieng.png' }
  ];

  const defenders = [
    { number: 2, name: 'Giorgos Vagiannidis', position: 'Defender', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/c4dd21e977d04c277ac49ef75177aaa05ff1bd6e/john.png' },
    { number: 3, name: 'Philipp Max', position: 'Defender', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/d548142d4e4083c57257b5a26f5faa76cbefc66f/ochieng.png' },
    { number: 5, name: 'Bart Schenkeveld', position: 'Defender', image: 'https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/kahata.png?raw=true' }
  ];

  const technical = [
    { name: 'Giorgos Vagiannidis', position: 'Head Coach', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/c4dd21e977d04c277ac49ef75177aaa05ff1bd6e/john.png' },
    { name: 'Philipp Max', position: 'Assistant Coach', image: 'https://raw.githubusercontent.com/JemoGithirwa4/Dimba-Itambe-player-images/d548142d4e4083c57257b5a26f5faa76cbefc66f/ochieng.png' },
    { name: 'Bart Schenkeveld', position: 'Physical Trainer', image: 'https://github.com/JemoGithirwa4/Dimba-Itambe-player-images/blob/main/kahata.png?raw=true' }
  ];

  const teamContent = (
    <>
      <div className="container pb-4">
        <PositionSection title="Goalkeepers" players={goalkeepers} />
        <PositionSection title="Defenders" players={defenders} />
      </div>
    </>
  );

  const technicalContent = (
    <div className="row">
      {technical.map((staff, index) => (
        <TechnicalStaff
          key={index}
          name={staff.name}
          position={staff.position}
          image={staff.image}
        />
      ))}
    </div>
  );

  const tabs = [
    { id: "squad", label: "Squad", content: teamContent },
    { id: "technical", label: "Technical Staff", content: technicalContent },
  ];

  return (
    <>
        <ClubNavbar />
        
        <main>
          <Tabs tabs={tabs} />
            <div></div>
        </main>

        <ClubFooter />
    </>
  );
}

export default Club;