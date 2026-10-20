import { useState } from 'react'
import { api } from './api'

const GENRES = ['PSYCHEDELIC_CLOUD_RAP', 'SOUL', 'BLUES', 'BRIT_POP']

function Operation({ title, children }) {
  return (
    <fieldset style={{ marginBottom: 12 }}>
      <legend>{title}</legend>
      {children}
    </fieldset>
  )
}

export default function SpecialOperations() {
  const [lessThan, setLessThan] = useState('')
  const [frontManGt, setFrontManGt] = useState('')
  const [removeId, setRemoveId] = useState('')
  const [nominateId, setNominateId] = useState('')
  const [genre, setGenre] = useState('SOUL')
  const [message, setMessage] = useState(null)

  async function run(url, options, format) {
    try {
      const body = await api(url, options)
      setMessage({ ok: true, text: format(body) })
    } catch (err) {
      setMessage({ ok: false, text: err.error ?? 'ошибка функции' })
    }
  }

  return (
    <div>
      <h1>дополнительные функции</h1>

      {message && (
        <div style={{ color: message.ok ? 'green' : 'red', marginBottom: 12 }}>
          {message.text}
        </div>
      )}

      <Operation title="среднее значение количества альбомов">
        <button onClick={() => run('/api/special/average-albums-count', {},
          b => `среднее: ${b.result}`)}>рассчитать</button>
      </Operation>

      <Operation title="количество групп с количеством альбомов меньше заданного">
        <input type="number" value={lessThan} onChange={e => setLessThan(e.target.value)} />{' '}
        <button disabled={lessThan === ''} onClick={() =>
          run(`/api/special/count-albums-less-than?value=${lessThan}`, {},
            b => `групп: ${b.result}`)}>посчитать</button>
      </Operation>

      <Operation title="количество групп с id фронтмена больше заданного">
        <input type="number" value={frontManGt} onChange={e => setFrontManGt(e.target.value)} />{' '}
        <button disabled={frontManGt === ''} onClick={() =>
          run(`/api/special/count-front-man-greater-than?value=${frontManGt}`, {},
            b => `групп: ${b.result}`)}>посчитать</button>
      </Operation>

      <Operation title="удалить из группы участника">
        <input type="number" placeholder="ID группы" value={removeId}
               onChange={e => setRemoveId(e.target.value)} />{' '}
        <button disabled={removeId === ''} onClick={() =>
          run(`/api/special/bands/${removeId}/remove-participant`, { method: 'POST' },
            b => `теперь участников: ${b.numberOfParticipants}`)}>удалить участника</button>
      </Operation>

      <Operation title="номинировать группу как лучшую в жанре">
        <input type="number" placeholder="ID группы" value={nominateId}
               onChange={e => setNominateId(e.target.value)} />{' '}
        <select value={genre} onChange={e => setGenre(e.target.value)}>
          {GENRES.map(g => <option key={g} value={g}>{g}</option>)}
        </select>{' '}
        <button disabled={nominateId === ''} onClick={() =>
          run(`/api/special/bands/${nominateId}/nominate?genre=${genre}`, { method: 'POST' },
            b => `номинация создана, id записи: ${b.nominationId}`)}>номинировать</button>
      </Operation>
    </div>
  )
}