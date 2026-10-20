export default function BandView({ band, onClose, onEdit }) {
  const row = (label, value) => (
    <tr><td><b>{label}</b></td><td>{value ?? '—'}</td></tr>
  )
  const fm = band.frontMan
  const loc = fm.location

  return (
    <div className="overlay">
      <div className="modal">
        <h2>группа #{band.id}</h2>
        <table cellPadding="3">
          <tbody>
            {row('название', band.name)}
            {row('создана', band.creationDate)}
            {row('жанр', band.genre)}
            {row('участники', band.numberOfParticipants)}
            {row('синглы', band.singlesCount)}
            {row('альбомы', band.albumsCount)}
            {row('описание', band.description)}
          </tbody>
        </table>

        <h3>координаты #{band.coordinates.id}</h3>
        <div>x = {band.coordinates.x}, y = {band.coordinates.y}</div>

        <h3>лучший альбом</h3>
        <div>{band.bestAlbum
          ? `${band.bestAlbum.id} ${band.bestAlbum.name}, треков: ${band.bestAlbum.tracks ?? '—'}`
          : '—'}</div>

        <h3>фронтмен #{fm.id}</h3>
        <table cellPadding="3">
          <tbody>
            {row('имя', fm.name)}
            {row('цвет глаз', fm.eyeColor)}
            {row('цвет волос', fm.hairColor)}
            {row('дата рождения', fm.birthday)}
            {row('национальность', fm.nationality)}
            {row('локация', loc ? `${loc.id} (${loc.x}; ${loc.y}; ${loc.z})` : null)}
          </tbody>
        </table>

        <p>
          <button onClick={() => onEdit(band)}>изменить</button>{' '}
          <button onClick={onClose}>закрыть</button>
        </p>
      </div>
    </div>
  )
}