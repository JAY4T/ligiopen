import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Pagination, Autoplay } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';

const kitImages = [
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKOcSZKDUetJBfV1oXzEfNsj1xvbsUBxzdGQ&s',
  'https://tuskerfc.com/wp-content/uploads/2024/08/1D9A0240-1-scaled.jpg',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRP8TY2y3pzCWbEaRzoGpu1BgRZgvit4qtx6yGjKKrPz-zxSbEzWCqHJrdYYHqMNB7zYUE&usqp=CAU',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQUWHNro2Hh2tHpznQzt5JzrTIVvVKs_TlarP8c-3g2v7adXX40ICOetzL39yhTcyYsIx4&usqp=CAU',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiaMKcOueNE6DzRbhTtKnviiLhMN1ZzdeKJWCNKbgh7RFntwfSPhojfNc4n8WDbaS-Ye4&usqp=CAU'
];

const KitsCarousel = () => {
  return (
    <div className="container py-4">
      <Swiper
        modules={[Navigation, Pagination, Autoplay]}
        navigation
        pagination={{ clickable: true }}
        autoplay={{ delay: 3000 }}
        loop={true}
        spaceBetween={30}
        slidesPerView={1}
        className="classic-kits-swiper"
      >
        {kitImages.map((img, index) => (
          <SwiperSlide key={index}>
            <img
              src={img}
              alt={`Classic Kit ${index + 1}`}
              className="img-fluid d-block mx-auto kit"
            />
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default KitsCarousel;