import React from "react";

function ClubTrophy() {
    return(
        <>
        <div className="container">
            <h2>Tusker FC Honours</h2>

            <div className="container-fluid bg-dark text-white py-5 mb-5">
                <div className="row align-items-center justify-content-center">
                    <div className="col-md-4 text-center mb-4 mb-md-0">
                    <img
                        src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjPCi77eMNKrSxmdo-v1bI7mt9gratbJp46w&s"             alt="Trophies"
                        className="img-fluid"
                        style={{ maxHeight: '200px' }}
                    />
                    </div>

                    <div className="col-md-6 text-start">
                        <h1 className="display-4 text-warning fw-bold">20</h1>
                        <h4 className="fw-bold">LEAGUE TITLES</h4>
                        <p>
                            <span className="text-info">
                            1908, 1911, 1952, 1956, 1957, 1965, 1967, 1993, 1994, 1996,
                            1997, 1999, 2000, 2001, 2003, 2007, 2008, 2009, 2011, 2013
                            </span>
                        </p>
                    </div>
                </div>
            </div>

            <div className="container-fluid bg-dark text-white py-5">
                <div className="row align-items-center justify-content-center">
                    <div className="col-md-4 text-center mb-4 mb-md-0">
                    <img
                        src="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBw8PDw0PDw8NDw0NDQ0NDQ0PDQ8NDQ0NFREWFhURFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMsNygtLi4BCgoKDg0OFQ8QFy0dFR0tLS8tLS0tLS0tLSstLS0rLS0tLS0rLS0tLSsrLystLSstLS0tLSsrLS0tLS0tKy0tK//AABEIAMsA+QMBEQACEQEDEQH/xAAbAAACAgMBAAAAAAAAAAAAAAAAAQIDBAUGB//EAEEQAAIBAgMFBQQHBgQHAAAAAAABAgMRBBIhBTFBUZEGEyJhoVJxgbEUFiMyQpLRBxUkU4LBQ3Oi4TRicoPS8PH/xAAaAQEBAAMBAQAAAAAAAAAAAAAAAQIDBAUG/8QANBEBAAICAQIEAgoBAwUAAAAAAAECAxESBCETMUFRFGEFIjJxgZGhscHw0WJy8RUjJEJS/9oADAMBAAIRAxEAPwDxs1MgAwoIAAAYAAAAAAAMAAAAAAAAAAAAAAQDsAgEAFQgAAAAGFIIYUEAAAAAAwABgAAAAAAAyBAAAUMgQAUAAQDKpBCAChBAAAMKAgCggAGAAAAAAMACgAAZEAUAAAEAAFACCAAKABAIAKAIApAMAAAAgYAAAAAAwABkUAADAAABAAAAAAAABCKoCEAgAoAAAACAAYAAAAAAwAACgBkABdh8PKo7RV+b4L3mF7xWNyyrSbTqG4p9m52vKol5KEmcc9dXfaHRHSz6yw8RsmcFdNPhZrK2+S8/Leb6dRWzXbBavza9o3tJFAAgAAAAAoQQAIAARQAAAAEAAwAAAAGAAAUAMgaAlTg5NRW+TUV727IkzqNq9M7MbCpU6SnLV5rXknGL43bfB2Pmes6nJkvMV8npYq1pGvV1uDwkLqMqSqeGTi4Rs3rduV3a+p5OXnEcuXHv6tkztz/aXZNOMp3i4qUFki2rtpvNubWnL3npdBnm0aid6/vq1Xnt3eebbwajaot+bLP+z9PVH0HT5N/Vly5qduUNQdTnIoAAAAQAUIgCoQAAgAoAAgAGAAAAAAMAAApkDAAMnZ7tVpN/zIfM15I3S33LXzh6NhdvQnRhScsjhfxX8Dg1ZwdteXQ+cnprUva8RuJ9Pn7u+lt17tjhe1CpNOLUs1qc5eJOC1a0e9Nvf5GOTo4zRHKPLuy5MXtDtaFZwWZOKvKbvZ2UW2l793xPVrTFEVika1DRaLTO5cDtXM4KWSUac/FBtPxq9r+e59DpxRHLXqwtvh3ag6nOQAUAAAgAACEUACAQAUAAAEDAAABgAAABTIABgMguwdPPUpRvbPUhG/K8krmN51WZ9ljzb3a0Vh8RiKCy3oVpwzJZc1nx8/M4ccTelbzHnDq5RE6X4SpZZr66XWjVr+ppvE703RpRtfaqlHKm29bWVt+lrG3Bhtvc+TC94iO3m2/brxYXZ0lFRi8JQaUU1CyvZJe6S+NzLo66nJ6/Wlz5J3Ovlv8AWP8ALhTvaQAigAAEAAACKgAQCAAAoAAgAGAAADACBhQAwAgaA2vZagqmOwUHe0sTRUrau2ZXNHU244rz8mVY7w2fbqjbFVavGVWS33T+9/4m2IiIivtCRaZ3LnI1WuL6kmkSy5ySm9WtHz49S6TlLuu2KzYHZTV/Ds+krWdkop/7+hx9HO/F/wB0l51kr84n+JcEzuYkAigAAEAAACCAoQAAigACAAAGAAADAAGFBAwGQNAb/sNpj8PJb4NyXx8N/hmv8Dm6uf8AtWbMcbtDaduaP2dKXHvLy+Kf9zX02ab2ttlekViHGna1B7mQek9qaNqaoK18Ps6nSS5SeGlN+r9Tl6WusFb+tp5fnPb9GrLb/wAmtfSI1+jzVnW2kEACKABAAAAggKEAAIAKAgAABgAAAwGQAUAMAAaIOp/Z5ScsY2knloVG7q7itPFHzvZfFnF106xfjDbi+02HahOdKsvYSmufhkr29WaekmImPm25YcQek5mVszDurXw9JK/e16VO3k5pP0Nea3HHa3tE/stY3MPQe0rVTF45rdmxEFrolGlKCX+kceGHHX2iv8OWLb6jv7z+zzM2uggEUAAAgAIRVAQAIBAAAFiDyjbPiRWsEDAAABgADIoCGFADIOv/AGaW+mSvv7ipb3pNnm/SkzGDt7t2H7cNztGkpOSe5ucXx8Ml+tjk6e0xr++TpyR2eeVKbjKUXvjJxfvTsz24ncbcMxrs6L9nmDdbaWFXClKVeWl/DBXObrPrY/D9bzEfnLPH2nfs3k6zqZ5ySjKoq1SSSSs3Co3dLjqdnUxqJ/vrDy+l3bNG/ef2l54ty9yI9GQEIoAABABUBAgAoQAAWC6ThAxmW6mPa3uzDk6fCYxueeCBgAAAwABkUAMAAZB1P7PKijjIt/ij3a1trUfdr1kjk62N4pZ0nVodTtKLi5x4xbW56Nc/ieNgnyehPeHB9oaVq7mlaNVKXlmtZ/r8T2+mtumvWHFlrq2/d0XYmHcYeviG3GpiqlLCULNqTgpp1XFrVaeG5tw08bq6R6Y+8/fPl/lhaeOOfmy8Uox9iEVTqqKVlH7layXuTj1NvVT5x937uDo4m2Wsx85eeLcvciO2QwhFAAAIAKEEAAAgACcImMy3UptdTia5l24qLchht18IYB1PBAAQADAYAAyACmDQsReMpZWNrxltOz85RqycXll3bcJW+7UU4yi+qMLTE6Y5aWikzry/y9A2hiYYmMcXSfgxNNTa0lKlVt44Nc1JtdOZ8/w8K80n0n9HoUtyrty+L2XUxDjD/ElJKCetrv8A909x3489cff0YXpyjTqtg0aFPENO0qOy8DXqxb8UZ1KcUs3m3Um7PjZHs/R9JpjnJb7Vp/v5OPNMb1HlDitqYuU6XdzXihUpRvzcaUbv0Zrtbe592nDTjmrPs08MHJpNcUn6GmctYnTtjp7TESmtmzZPHqfD2TWyp80T4iGPgTCcdjyf4kT4iGM45hZHYb5k+IYTXSxbDXGa6j4iWEyktiU+M+iJ49mPKT/c9Lm+ti+NZOUk9mUVxXUni2XlKDwVBcS87m5J4eguDfoOV17l3VH2V8WOVmdaTK2EKVt0ehrm1noYenvMD7NcPkjHdnTXpbwM0OS6ju2fDWc4ek8AwoACBgNBdJRRjMtlap5UY7ls4QlGKJMy2VpVYoxMdy3RTGkspj3bIjFCxSj5GOpbotihk4GtFVIeby9dP7k1Md2Oe2K+O1I85htNi4+OGilVzSw1aUl4X46NRfdlHn4d6e+3kasuOM1pr5Wh5GLJNUsdt2Mab7hydR3z1ZRcVTWbL4U/etfQY+l42i1vwbrZd1mIZWxKlSWGq573xso0YzatpGpv9Voj0Piq48c113jbR4c2lp9pQcalWk0nJOpUz75WdNWT6s0UyRekWj1SKcck/JjPFNNpRdlovcjV4cT329D4iYiI0Ppc+EGPCr7tc57T6JLEVuEBwp7tU5beySq4j2S8aNc5JP8AiX+H5jWNrmyXcYt8LfAbxtc2gfQsW+JeeNOUF+68S+Mug8Whyg/3JiHxkPGpBzgvq/X9p9R8RU5wkuzlXm/UnxFVjJC2PZqp/wAxjPUQ3VzxDJp9nZr/AOmqczsp9IcY0n9Xpc/Qnisp+kz+rsub6DxU/wCpuJPVeeYAAyAQWISyk2zispxgzGbNlccyn3bMeUNkYbJxosxm7dXprSsWHMebfXopTjhTGcjbXoNrY4MxnK6K/R0JrBox8Vtj6Pqrq3zVaTXhclXp8WpNO9uXibXvNsa1F4+58/1OHws18c+n/JVsPlppqTt3eeSe93qLTySbRlS+7TEx6tMxqOzuKU77O2U43/4jK7JK32kW/T5HJftXJ+P7Mq/a/JzUaUqmLxbk7rvKdOPlHPa3RG2JimGmvYrHLJr3luKey4eXQ4fFl6dqVhl0tlQ5E8WWi8Qy6eyqfJdCeJLkvLKp7Np8l0HOXNaV8dn0+Rdy0zKxYKn7K6DctZrDQ5LoNgdGHJDYj3cOSCotQ5LoEJyjyQEHVj5DSouvHkXSK5Ypcho0X0vyXUaNPIT3XWAAgYE4IktlI7rUjVt1xC2CMZdOOFyRg6oqsijGW+lVsUYS6a1WRRjLfWFqMZbYgyMmLtC8VGqt9J+Lzpya+Usv5mdODVomkvn/AKaw6mmePun+FmMpXjDLuV4P/pluNeK8xM7eRau9abrY+KU44XCRvmod9UrXWivH7Oz58/ejX1M6xTf3XHH19KezdLM8dVasnjauRfF+mr6F6m3GlK/6YXpo3kmfbboqVM4HZe7LpQDlvdkRXkVyXstinyK0WlOzMmqRlfMqIuDAg6b5jYTo+bGxCVBc2UR7iJAdxEu0LuIjaouhHkF2O5jyRDbxs951AgaBCSRGcQnFGMy3UquSNbrrC2CMJdeOFqRi6YqsijGW+sLoowl0VhNGLbCaIzMhtdCkpJxkrxknGS5xasyRkmlomHJ1VIy47Y58pY+y6FWanSUHUnQahOyu7fglbk0kbOotSsxfeos+XrW0TNZjvDoMBsZ4SVbFVZqV6Ki4JaQ8V5O/HSK6s87J1UZ4rhrHq2RTjM3kuy9J/RKUpK0q0qteXHSVSTj6a/E7OsmPE4x6REMOnnVZn3b2lA5C9mRBBz2ldFGTntKyxWqZMrEBCbGxFlCAhIgQCZREBMCIV4ye86gBKJJZVhYkYN8QnFGEy30quijCZddarYoxl00qsRi6awtgjGW+lVqRg3xCSIziDRFThG5JljazPw9M0WlxZLsvBR7rEU62aMYzi6NbM1FSjZuD14qSt/US9ZzYZx+cx3j+Xj9VxreMnv2ls9vyjUw84Rkr1k6ekk3ByWjlZ6Grpejy48kZJjtEuXLmprjvzXYSnFQgo/djCEY8PCkkvQyvMzaZnzTyjUMyCMWm1l9NGUOa9lyRWmZAYk2UIBBCYVFhEWUBBFlCYEWUIK8YPddRkWE4GMttIXJGG3VFVkYmEy6a41sUYy6KwtijGXTSFkUYOmtVkUYy3VhYjFtAE6cbkmWNp0zaNI02s5cl2fRgapcV7niaUZwbaU9Gox00k3Zy9yV9ebXI20+q87LbxJ1HlBVsNGvShGUKUJSg5xlGMY2cZuMtF5K/wMrZJxzy32j+WjjtldntoOd6NW3ew0jJf4kf1Mc2OPt18parTqHQRiaIc9rLYIyaJlYVgTAiyBFCCBlECCMgAoTAiyiLARB40j3nZCaRjttiqyCMJlvx1WWMNujjpbFGMuqsdlkUYt9YWwRhLppC1IxdEQkiNkQkRmaIkyyKETXaWi9mwoo0y47y2WDw7nOEFvnJR5acfS5jHbvLhzW7M6jsyNSWKt4ZXxCzttrLCneCjwte/Ul7zukT/ZcuPURZN4GNCpTcpJUPHSzSWeVmnd6ecrIk2nJW1fXW2M9u7mqeaOInaLjUi779zWqT+KS+LO2kROKJ9HLN48SaT6u1w01OMZrdOKkvc0ckxqdOO06nS6xWoBSYEWAkEDGxFlCIIyKEAmBEoTII3KbeORR7ku+sLEjCZb4qsijGZb6VWpGDqrWZhZFGMt9YWxRhLprVbFGMuikJpGLdEJEZmgSup0jCbNV7wzKVI0zLltkbDD0TXtx5Mjf9naF629LLSqSu7PhbiYZJ1jmfVw5L7mGywylXp024xpwtWoTkm4qUlVUVDTyVvh7zTaZm2o8/ua69l21aEHRrNRUp0oqvTUU24zk5ZHzstJfE20isdRNKzuPL8tMZ3xeb4ivPvatWX3s8akvP7SN/mezGLjTg8687y1n5uq7M181KceNGtOH9LtOL6SPPzRqYn3TPGrb922NbQApARZAioAIsoQEWAgaJgRYCkwIlHj0Ue3L0qxpbEwlvqtgjCXXihckYOuKpxRjMt1arYoxmXRWqyKMZb6wmYtgAtpR1MZlrvaNNjQgc8y4cl2XSpGMy5rXZ1CBi5L2anaO2KlCo8jUlK8JRlKaikrey0+J3Vw1tjjbz5y28S0ekf4X4XaF3OajTUlmqy1rOLcnq2nLm9976mvjOvLsz5fJs4bWlKOXNTd5atxqSbkrO2r5XM+nxV594JtMxtDtHhqEas1TgnDJRqRuofdzxjO70zaqfoehnw0xzuv7y4r3vM636MSl9l9HcLxf0+lSqW/FGUV4X5XZw5dTaY/0s8Fpvg+t38/08nWXOJyFcoGyKi2ArhCuUJgRYCYUghFEWwIsBEV5Eke1MvXiqyKMJbqVXRRhLspCxGLphZAxlvpC2JjLfVNGLbCQVOETGZYWszaNM0Wly3u2FCma3DkuyqcCOe1mRBxX4or+pDUue12uxuyKVWeZ4jDwvZ2k3Jp3Wu9cE+psnq7UjjGOZaK4OVpty7SysNhcJCKUq+GlJSzZ7q7ja2VrNu39Tntny2jtjn+/g31wxE7mzIlVwXi+3gk2/CnFqzadtb8i1z9TFuUYyuGsRqbbQ2hj8BUbeaK8MYWgpblxva9zfl6rrMs/YiI/vza/hsW9zMsaWNwcmlnnlVaGI0UtKkGrcPIxpHU2t315a/BjOPHjpqrYvtLhfam/+3I3fD5Hm+FZGXaXDL+Z8If7l+HuvhWR+s2H4Kq/6V+o+GueFZCXaaj7FXpH9R8Nb3PBsj9ZqX8ur/o/Uvw1vdfBlF9p6f8up1iX4WfdPBn3Ql2ojwpS/Ol/Yvws+6+DPu3WHrZ4Qna2eEZ232ur2Oe0amYapjUpMgVyIVwEyqi2ERuNK8mij15e3SFsUYy6q1WoxdNYTRi3xC2BhLopCUpW4J9REbcfW9RkxWrFJ1EwI12uEfyp/McXDPXZ5/wDb9g6z8vypE4sZ6vN/9SarS5jjDCeoyz52n8z76XtS6snGPZhOS0+shV5e1L8zLxhjyknUfmxxNovUqCMhMJtO5iyFwC4NnCdr+4zp5teTyWQ0V+huc5QlfR8fmECeVgW3IpXCE2UK5R3Gypfw9D/Kh8jzMv25clo7yyXIwQswCcgE5ARcgI3KPK4HrS96iyJhLqxrEYumFkTGW+q2JjLfUqpavK+k/t1+5BFeaaIgQDiAMQpBE0Yr6IMyYylFklYMKAGjKnmwyeS+pu6G1zqo70UhZV3IkApvQShhSYQijtdjv+Hof5aPNzfbly385ZTZrYkmVAGSLEIiWAgP/9k="             alt="Trophies"
                        className="img-fluid"
                        style={{ maxHeight: '200px' }}
                    />
                    </div>

                    <div className="col-md-6 text-start">
                        <h1 className="display-4 text-warning fw-bold">2</h1>
                        <h4 className="fw-bold">AFC Champions League</h4>
                        <p>
                            <span className="text-info"> 2011, 2013
                            </span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        </>
    );
}

export default ClubTrophy;